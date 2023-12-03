package com.zhibai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.lzhpo.chatgpt.OpenAiClient;
import com.lzhpo.chatgpt.entity.chat.ChatCompletionRequest;
import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.common.Constants;
import com.zhibai.ai.configuration.ProxyProperties;
import com.zhibai.ai.enums.GptErrorEnum;
import com.zhibai.ai.enums.SceneTypeEnum;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.listener.ChatSseEventSourceListener;
import com.zhibai.ai.manager.ConversationHistoryManager;
import com.zhibai.ai.manager.ConversationTitleManager;
import com.zhibai.ai.manager.SceneInfoManager;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.model.gptModel.GptStreamRequest;
import com.zhibai.ai.model.gptModel.GptStreamResp;
import com.zhibai.ai.model.req.SceneAskReq;
import com.zhibai.ai.model.req.SceneStartReq;
import com.zhibai.ai.model.resp.SceneInfoResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.ChatGPTService;
import com.zhibai.ai.util.RedisUtil;
import com.zhibai.ai.util.UuidUtils;
import com.zhibai.ai.model.domain.*;
import com.zhibai.ai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 16:58
 * @description
 **/
@Slf4j
@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    @Resource
    private SceneInfoManager sceneInfoManager;

    @Resource
    private UserVipInfoManager userVipInfoManager;

    @Resource
    private ConversationHistoryManager conversationHistoryManager;

    @Resource
    private ConversationTitleManager conversationTitleManager;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ProxyProperties properties;

    @Override
    public List<SceneInfoResp> queryAll() {
        List<SceneInfoResp> result = new ArrayList<>();

        List<SceneInfoDO> sceneInfoList = sceneInfoManager.queryAll();
        if (CollectionUtils.isNotEmpty(sceneInfoList)) {
            sceneInfoList.forEach(sceneInfoDO -> {
                SceneInfoResp sceneInfoResp = new SceneInfoResp();
                BeanUtils.copyProperties(sceneInfoDO, sceneInfoResp);
                sceneInfoResp.setSceneType(SceneTypeEnum.getDescByType(sceneInfoDO.getSceneType()));
                result.add(sceneInfoResp);
            });
        }
        return result;
    }

    @Override
    public void start(SceneStartReq req, HttpServletResponse response) {
        SceneInfoDO sceneInfoDO = sceneInfoManager.queryByUserId(req.getSceneId());
        if (sceneInfoDO == null) {
            throw new AiServerException(AiServerExceptionEnum.SCENE_NOT_EXISTS);
        }

        String prompt;
        if (CollectionUtils.isNotEmpty(req.getFillInfo())) {
            prompt = MessageFormat.format(sceneInfoDO.getScenePrompt(), req.getFillInfo().toArray());
        } else {
            prompt = sceneInfoDO.getScenePrompt();
        }

        String key = RedisUtil.buildKey(String.valueOf(req.getUserId()), String.valueOf(req.getSceneId()), Constants.BATCH_SUFFIX);
        String batchNo = UuidUtils.getUUID();
        redisUtil.set(key, batchNo, 86400);

        SceneAskReq sceneAskReq = new SceneAskReq();
        sceneAskReq.setUserId(req.getUserId());
        sceneAskReq.setBatchNo(batchNo);
        sceneAskReq.setSceneId(req.getSceneId());
        sceneAskReq.setInput(prompt);
        boolean result = ask(sceneAskReq, response);
        if (result){
            ConversationTitleDO record = new ConversationTitleDO();
            record.setUserId(req.getUserId());
            record.setBatchNo(batchNo);
            record.setShopType(ShopEnum.TEXT.getType());
            List<String> fillInfo = sceneInfoDO.getFillInfo();
            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(fillInfo)) {
                for (int i = 0; i < fillInfo.size(); i++) {
                    if (i == (fillInfo.size() - 1)) {
                        sb.append(fillInfo.get(i)).append(":").append(req.getFillInfo().get(i));
                    } else {
                        sb.append(fillInfo.get(i)).append(":").append(req.getFillInfo().get(i)).append(",");
                    }
                }
            } else {
                sb.append("问题：").append(sceneInfoDO.getScenePrompt());
            }
            record.setHistoryTitle(sb.toString());
            conversationTitleManager.insert(record);
        }
    }

    @Override
    public boolean ask(SceneAskReq req, HttpServletResponse response) {
        if (StringUtils.isBlank(req.getInput())){
            throw new AiServerException(AiServerExceptionEnum.VALIDATION_ERROR);
        }
        UserVipInfoDO userVipInfoDO = checkBalance(req.getUserId(), response);
        if (userVipInfoDO == null) {
            return false;
        }

        boolean result = stream(req.getInput(), req.getUserId(), req.getSceneId(), req.getBatchNo(), response);
        // 调用成功则对应次数减一
        if (result && userVipInfoDO.getTextRemainNumber() > 0) {
            userVipInfoDO.setTextRemainNumber(userVipInfoDO.getTextRemainNumber() - 1);
            userVipInfoManager.update(userVipInfoDO);
        }
        return result;
    }

    @Override
    public SseEmitter askSse(SceneAskReq req, OpenAiClient openAiClient) {
        if (StringUtils.isBlank(req.getInput())){
            SseEmitter sseEmitter = new SseEmitter();
            sseEmitter.completeWithError(new AiServerException(AiServerExceptionEnum.VALIDATION_ERROR));
            return sseEmitter;
        }
        SseEmitter sseEmitter = new SseEmitter();
        UserVipInfoDO userVipInfoDO = checkBalanceSse(req.getUserId(), sseEmitter);
        if (userVipInfoDO == null) {
            return sseEmitter;
        }
        return streamSse(req.getInput(), req.getUserId(), req.getSceneId(), req.getBatchNo(), userVipInfoDO, sseEmitter, openAiClient);
    }

    @Override
    public SseEmitter startSse(SceneStartReq req, OpenAiClient openAiClient) {
        if (UserThreadLocal.getUserId() != null) {
            req.setUserId(UserThreadLocal.getUserId());
        }
        SceneInfoDO sceneInfoDO = sceneInfoManager.queryByUserId(req.getSceneId());
        if (sceneInfoDO == null) {
            SseEmitter sseEmitter = new SseEmitter();
            sseEmitter.completeWithError(new AiServerException(AiServerExceptionEnum.SCENE_NOT_EXISTS));
            return sseEmitter;
        }

        String prompt;
        if (CollectionUtils.isNotEmpty(req.getFillInfo())) {
            prompt = MessageFormat.format(sceneInfoDO.getScenePrompt(), req.getFillInfo().toArray());
        } else {
            prompt = sceneInfoDO.getScenePrompt();
        }

        String key = RedisUtil.buildKey(String.valueOf(req.getUserId()), String.valueOf(req.getSceneId()), Constants.BATCH_SUFFIX);
        String batchNo = UuidUtils.getUUID();
        redisUtil.set(key, batchNo, 86400);

        SceneAskReq sceneAskReq = new SceneAskReq();
        sceneAskReq.setUserId(req.getUserId());
        sceneAskReq.setBatchNo(batchNo);
        sceneAskReq.setSceneId(req.getSceneId());
        sceneAskReq.setInput("你是誰");
        // 内部发起请求，并校验
        return askSse(sceneAskReq, openAiClient);
    }

    private UserVipInfoDO checkBalance(Long userId, HttpServletResponse response) {
        UserVipInfoDO userVipInfoDO = userVipInfoManager.queryByUserIdAndType(userId, ShopEnum.TEXT.getType());
        if (userVipInfoDO == null || LocalDateTime.now().isAfter(userVipInfoDO.getExpiredTime()) || !(Constants.INFINITE.equals(userVipInfoDO.getTextRemainNumber()) || userVipInfoDO.getTextRemainNumber() > 0)) {
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                writer.write("{\n" +
                        "    \"errorCode\": \"1002\",\n" +
                        "    \"errorMessage\": \"可用次数不足/暂无会员/会员已过期!\",\n" +
                        "    \"success\": false,\n" +
                        "    \"data\": null\n" +
                        "}");
                writer.flush();
            } catch (Exception e) {
                log.error("ChatGPTServiceImpl#ask exception", e);
            }
            return null;
        }
        return userVipInfoDO;
    }

    private UserVipInfoDO checkBalanceSse(Long userId, SseEmitter sseEmitter) {
        UserVipInfoDO userVipInfoDO = userVipInfoManager.queryByUserIdAndType(userId, ShopEnum.TEXT.getType());
        if (userVipInfoDO == null || LocalDateTime.now().isAfter(userVipInfoDO.getExpiredTime()) || !(Constants.INFINITE.equals(userVipInfoDO.getTextRemainNumber()) || userVipInfoDO.getTextRemainNumber() > 0)) {
            sseEmitter.completeWithError(new RuntimeException("{\n" +
                    "    \"errorCode\": \"1002\",\n" +
                    "    \"errorMessage\": \"可用次数不足/暂无会员/会员已过期!\",\n" +
                    "    \"success\": false,\n" +
                    "    \"data\": null\n" +
                    "}"));
            return null;
        }
        return userVipInfoDO;
    }

    private boolean stream(String input, Long userId, Long sceneId, String batchNo, HttpServletResponse response) {
        if (StringUtils.isBlank(batchNo)){
            String batchNoKey = RedisUtil.buildKey(String.valueOf(userId), String.valueOf(sceneId), Constants.BATCH_SUFFIX);
            batchNo = redisUtil.getString(batchNoKey);
        }

        GptStreamRequest gptMessage = new GptStreamRequest();
        gptMessage.setStream(true);
        gptMessage.setModel(properties.getOpenai().getModel());
        gptMessage.setMaxToken(properties.getOpenai().getMaxTokens());
        gptMessage.setTemperature(properties.getOpenai().getTemperature());

        List<GptStreamRequest.Message> messages = new ArrayList<>();
        ConversationHistoryDO conversationHistoryDO = conversationHistoryManager.queryNewByBatchNoAndShopType(batchNo, ShopEnum.TEXT.getType());
        if (conversationHistoryDO != null) {
            HistoryInfoDO historyInfoDO = JSON.parseObject(conversationHistoryDO.getHistoryInfo(), HistoryInfoDO.class);
            if (historyInfoDO.getQ().length() > 500) {
                messages.add(new GptStreamRequest.Message("assistant", historyInfoDO.getQ().substring(0, 400)));
            } else {
                messages.add(new GptStreamRequest.Message("assistant", historyInfoDO.getQ()));
            }
        }
        messages.add(new GptStreamRequest.Message("user", input));
        gptMessage.setMessages(messages);

        HttpRequest client = HttpRequest.post(properties.getOpenai().getGptProxyUrl() + "/v1/chat/completions")
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .header(Header.AUTHORIZATION, "Bearer " + properties.getOpenai().getGptApiKey())
                .timeout(60000)
                .body(JSON.toJSONString(gptMessage));


        String line;
        StringBuilder sb = new StringBuilder();
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.executeAsync().bodyStream()))) {
            while ((line = reader.readLine()) != null) {
                line = StrUtil.replace(line, "data: ", "");
                if (StrUtil.isBlank(line)) {
                    continue;
                }

                if (!StrUtil.equals("[DONE]", line)) {
                    GptStreamResp chatCompletionResponse = JSONUtil.toBean(line, GptStreamResp.class);
                    //被封场景
                    if (chatCompletionResponse.getError() != null && GptErrorEnum.account_deactivated.getCode().equals(chatCompletionResponse.getError().getCode())){
                        writer.write("{\n" +
                                "    \"errorCode\": \"1003\",\n" +
                                "    \"errorMessage\": \"违禁提问，请重新输入\",\n" +
                                "    \"success\": false,\n" +
                                "    \"data\": null\n" +
                                "}");
                        writer.flush();
                        return false;
                    }
                    if (!StrUtil.equals("stop", chatCompletionResponse.getChoices().get(0).getFinishReason())) {
                        String content = chatCompletionResponse.getChoices().get(0).getDelta().getContent();
                        if (StrUtil.isBlank(content)) {
                            continue;
                        }
                        sb.append(content);
                        writer.write(content);
                        writer.flush();
                    }
                }
            }

            // 保存历史对话记录
            ConversationHistoryDO record = new ConversationHistoryDO();
            record.setUserId(userId);
            record.setShopType(ShopEnum.TEXT.getType());
            record.setBatchNo(batchNo);
            record.setHistoryInfo(JSON.toJSONString(new HistoryInfoDO(input, sb.toString())));
            conversationHistoryManager.insert(record);
            reader.close();
            return true;
        } catch (Exception e) {
            log.error("ChatGPTServiceImpl#stream exception", e);
            return false;
        }
    }

    private SseEmitter streamSse(String input, Long userId, Long sceneId, String batchNo, UserVipInfoDO userVipInfoDO, SseEmitter sseEmitter, OpenAiClient openAiClient) {
        if (StringUtils.isBlank(batchNo)){
            String batchNoKey = RedisUtil.buildKey(String.valueOf(userId), String.valueOf(sceneId), Constants.BATCH_SUFFIX);
            batchNo = redisUtil.getString(batchNoKey);
        }

        // 监听结果
        ChatCompletionRequest request = ChatCompletionRequest.create(input);
        try {
            ChatSseEventSourceListener listener = new ChatSseEventSourceListener(sseEmitter);
            openAiClient.streamChatCompletions(request, listener);
            try {
                listener.getCountDownLatch().await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("等待超时: {}, input: {}", StringUtil.getExceptionStackTrace(e), input);
            }

            String content = listener.getStringBuffer().toString();
            // 保存历史对话记录
            ConversationHistoryDO record = new ConversationHistoryDO();
            record.setUserId(userId);
            record.setShopType(ShopEnum.TEXT.getType());
            record.setBatchNo(batchNo);
            record.setHistoryInfo(JSON.toJSONString(new HistoryInfoDO(input, content)));
            conversationHistoryManager.insert(record);

            // 监听流式调用成功则对应次数减一
            if (userVipInfoDO.getTextRemainNumber() > 0) {
                userVipInfoDO.setTextRemainNumber(userVipInfoDO.getTextRemainNumber() - 1);
                userVipInfoManager.update(userVipInfoDO);
            }
            log.info("会话已完成");
        } catch (Exception e) {
            log.error(StringUtil.getExceptionStackTrace(e));
            sseEmitter.completeWithError(e);
        }
        return sseEmitter;
    }

}
