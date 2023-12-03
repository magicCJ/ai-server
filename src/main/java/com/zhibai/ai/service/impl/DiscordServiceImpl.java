package com.zhibai.ai.service.impl;


import com.alibaba.fastjson.JSON;
import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.common.Constants;
import com.zhibai.ai.configuration.ProxyProperties;
import com.zhibai.ai.enums.Action;
import com.zhibai.ai.enums.ImgStatus;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.enums.TaskStatus;
import com.zhibai.ai.manager.ConversationHistoryManager;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.ConversationHistoryDO;
import com.zhibai.ai.model.domain.HistoryInfoDO;
import com.zhibai.ai.model.domain.UserVipInfoDO;
import com.zhibai.ai.model.dto.DescribeDTO;
import com.zhibai.ai.model.dto.SubmitDTO;
import com.zhibai.ai.model.dto.TaskDTO;
import com.zhibai.ai.model.query.HistoryPageQuery;
import com.zhibai.ai.model.req.ImgHistoryPageReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.TaskReq;
import com.zhibai.ai.service.DiscordService;
import com.zhibai.ai.translate.GPTTranslate;
import com.zhibai.ai.util.CosUtils;
import com.zhibai.ai.util.DateUtils;
import com.zhibai.ai.util.RedisUtil;
import com.zhibai.ai.util.UuidUtils;
import com.zhibai.ai.wrapper.MjProxyWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordServiceImpl implements DiscordService {

    @Resource
    private ProxyProperties properties;

    @Resource
    private MjProxyWrapper mjProxyWrapper;

    @Resource
    private ConversationHistoryManager conversationHistoryManager;

    @Resource
    private UserVipInfoManager userVipInfoManager;

    @Resource
    private GPTTranslate gptTranslate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CosUtils cosUtils;

    @Override
    public Result<HistoryInfoResp> result(Long userId, Long historyId) {
        ConversationHistoryDO conversationHistoryDO = conversationHistoryManager.queryById(historyId, userId);
        if (conversationHistoryDO == null) {
            return Result.failed(AiServerExceptionEnum.DATE_NOT_EXISTS);
        }
        String historyInfo = conversationHistoryDO.getHistoryInfo();
        if (StringUtils.isNotBlank(historyInfo)) {
            HistoryInfoDO historyInfoDO = JSON.parseObject(historyInfo, HistoryInfoDO.class);
            // 超过10分钟还未生成完成，则默认生成失败
            if (DateUtils.moreThanTenMinutes(conversationHistoryDO.getGmtCreate())
                    && ImgStatus.IN_PROGRESS.getStatus().equals(historyInfoDO.getStatus())){
                historyInfoDO.setStatus(ImgStatus.FAILURE.getStatus());
                historyInfoDO.setA("很抱歉，似乎有一些问题导致了您的绘图失败。如果该问题持续出现，请联系客服。");
                conversationHistoryDO.setHistoryInfo(JSON.toJSONString(historyInfoDO));
                conversationHistoryManager.update(conversationHistoryDO);
            }
            HistoryInfoResp historyInfoResp = new HistoryInfoResp();
            BeanUtils.copyProperties(historyInfoDO, historyInfoResp);
            historyInfoResp.setId(conversationHistoryDO.getId());
            historyInfoResp.setCreateTime(DateUtils.format(conversationHistoryDO.getGmtCreate()));
            return Result.success(historyInfoResp);
        }
        return Result.failed(AiServerExceptionEnum.DATE_NOT_EXISTS);
    }

    @Override
    public Result<PageInfo<HistoryInfoResp>> queryAllHistory(ImgHistoryPageReq req) {
        HistoryPageQuery query = new HistoryPageQuery();
        query.setUserId(req.getUserId());
        query.setShopType(ShopEnum.PICTURE.getType());
        int total = conversationHistoryManager.queryHistoryCount(query);
        if (total == 0) {
            return Result.success(new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total));
        }

        query.setPage(req.getCurrentPage(), req.getPageSize());
        List<ConversationHistoryDO> conversationHistoryList = conversationHistoryManager.queryHistoryDetail(query);
        List<HistoryInfoResp> data = new ArrayList<>();
        conversationHistoryList.forEach(conversationHistoryDO -> {
            HistoryInfoDO historyInfoDO = JSON.parseObject(conversationHistoryDO.getHistoryInfo(), HistoryInfoDO.class);
            // 超过10分钟还未生成完成，则默认生成失败
            if (DateUtils.moreThanTenMinutes(conversationHistoryDO.getGmtCreate())
                    && ImgStatus.IN_PROGRESS.getStatus().equals(historyInfoDO.getStatus())){
                historyInfoDO.setStatus(ImgStatus.FAILURE.getStatus());
                historyInfoDO.setA("很抱歉，似乎有一些问题导致了您的绘图失败。如果该问题持续出现，请联系客服。");
                conversationHistoryDO.setHistoryInfo(JSON.toJSONString(historyInfoDO));
                conversationHistoryManager.update(conversationHistoryDO);
            }
            HistoryInfoResp historyInfoResp = new HistoryInfoResp();
            BeanUtils.copyProperties(historyInfoDO, historyInfoResp);
            historyInfoResp.setId(conversationHistoryDO.getId());
            historyInfoResp.setCreateTime(DateUtils.format(conversationHistoryDO.getGmtCreate()));
            data.add(historyInfoResp);
        });
        return Result.success(new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total, data));
    }

    @Override
    public Result<Long> imagine(Long userId, String prompt) {
        if (StringUtils.isBlank(prompt)) {
            return Result.failed(AiServerExceptionEnum.VALIDATION_ERROR);
        }
        UserVipInfoDO userVipInfoDO = checkBalance(userId);
        // 将中文翻译成英文
        String englishPrompt = gptTranslate.translateToEnglish(prompt);

        String batchNo = UuidUtils.getUUID();
        SubmitDTO submitDTO = new SubmitDTO();
        submitDTO.setPrompt(englishPrompt);
        submitDTO.setAction(Action.IMAGINE);
        submitDTO.setState(batchNo);
        submitDTO.setNotifyHook(properties.getNotifyHook());
        String taskId = mjProxyWrapper.submit(submitDTO);
        if (taskId == null) {
            return Result.failed(AiServerExceptionEnum.IMG_CREATE_FAILED);
        }

        ConversationHistoryDO conversationHistoryDO = new ConversationHistoryDO();
        conversationHistoryDO.setUserId(userId);
        conversationHistoryDO.setShopType(ShopEnum.PICTURE.getType());
        conversationHistoryDO.setTaskId(taskId);
        conversationHistoryDO.setBatchNo(batchNo);
        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(new HistoryInfoDO(prompt, ImgStatus.IN_PROGRESS.getStatus(), Action.IMAGINE.name())));
        conversationHistoryManager.insert(conversationHistoryDO);

        // 调用成功则对应次数减一
        if (userVipInfoDO.getSpeedRemainNumber() > 0) {
            userVipInfoDO.setSpeedRemainNumber(userVipInfoDO.getSpeedRemainNumber() - 1);
            userVipInfoManager.update(userVipInfoDO);
        }

        TaskDTO taskDTO = new TaskDTO(taskId, prompt);
        redisUtil.set(String.valueOf(conversationHistoryDO.getId()), JSON.toJSONString(taskDTO), 3600);
        return Result.success(conversationHistoryDO.getId());
    }

    @Override
    public Result<Long> upscale(Long userId, Long historyId, Integer index) {
        return updateImg(userId, historyId, index, Action.UPSCALE);
    }


    @Override
    public Result<Long> variation(Long userId, Long historyId, Integer index) {
        return updateImg(userId, historyId, index, Action.VARIATION);
    }

    private UserVipInfoDO checkBalance(Long userId) {
        UserVipInfoDO userVipInfoDO = userVipInfoManager.queryByUserIdAndType(userId, ShopEnum.PICTURE.getType());
        // 用户没有绘图VIP
        if (userVipInfoDO == null || LocalDateTime.now().isAfter(userVipInfoDO.getExpiredTime()) || !(Constants.INFINITE.equals(userVipInfoDO.getSpeedRemainNumber()) || userVipInfoDO.getSpeedRemainNumber() > 0)) {
            throw new AiServerException(AiServerExceptionEnum.AVAILABLE_FREQUENCY_DEFICIENCY);
        }
        return userVipInfoDO;
    }

    public Result<Long> updateImg(Long userId, Long historyId, Integer index, Action action) {
        UserVipInfoDO userVipInfoDO = checkBalance(userId);
        TaskDTO taskDTO = getTaskDTO(userId, historyId);

        String batchNo = UuidUtils.getUUID();
        SubmitDTO submitDTO = new SubmitDTO();
        submitDTO.setAction(action);
        submitDTO.setNotifyHook(properties.getNotifyHook());
        submitDTO.setTaskId(taskDTO.getId());
        submitDTO.setIndex(index);
        submitDTO.setState(batchNo);

        String newTaskId = mjProxyWrapper.submit(submitDTO);
        if (newTaskId == null) {
            return Result.failed(AiServerExceptionEnum.IMG_CREATE_FAILED);
        }

        ConversationHistoryDO conversationHistoryDO = new ConversationHistoryDO();
        conversationHistoryDO.setUserId(userId);
        conversationHistoryDO.setShopType(ShopEnum.PICTURE.getType());
        conversationHistoryDO.setTaskId(newTaskId);
        conversationHistoryDO.setBatchNo(batchNo);
        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(new HistoryInfoDO(taskDTO.getPrompt(), ImgStatus.IN_PROGRESS.getStatus(), action.name())));
        conversationHistoryManager.insert(conversationHistoryDO);

        // 调用成功则对应次数减一
        if (userVipInfoDO.getSpeedRemainNumber() > 0) {
            userVipInfoDO.setSpeedRemainNumber(userVipInfoDO.getSpeedRemainNumber() - 1);
            userVipInfoManager.update(userVipInfoDO);
        }

        TaskDTO newTaskDTO = new TaskDTO(newTaskId, taskDTO.getPrompt());
        redisUtil.set(String.valueOf(conversationHistoryDO.getId()), JSON.toJSONString(newTaskDTO), 3600);
        return Result.success(conversationHistoryDO.getId());
    }

    @Override
    public Result<Long> reset(Long userId, Long historyId) {
        TaskDTO taskDTO = getTaskDTO(userId, historyId);
        return imagine(userId, taskDTO.getPrompt());
    }

    private TaskDTO getTaskDTO(Long userId, Long historyId) {
        System.out.println(redisUtil.getString(String.valueOf(historyId)));
        TaskDTO taskDTO = JSON.parseObject(redisUtil.getString(String.valueOf(historyId)), TaskDTO.class);
        if (taskDTO == null) {
            ConversationHistoryDO conversationHistoryDO = conversationHistoryManager.queryById(historyId, userId);
            if (conversationHistoryDO == null) {
                throw new AiServerException(AiServerExceptionEnum.VALIDATION_ERROR);
            }
            String historyInfo = conversationHistoryDO.getHistoryInfo();
            HistoryInfoDO historyInfoDO = JSON.parseObject(historyInfo, HistoryInfoDO.class);
            taskDTO = new TaskDTO(conversationHistoryDO.getTaskId(), historyInfoDO.getQ());
            redisUtil.set(String.valueOf(conversationHistoryDO.getId()), JSON.toJSONString(taskDTO), 3600);
        }
        return taskDTO;
    }

    @Override
    public Result<Long> describe(Long userId, MultipartFile file) {
        UserVipInfoDO userVipInfoDO = checkBalance(userId);

        String base64Img = getBase64(file);
        if (StringUtils.isBlank(base64Img)) {
            return Result.failed(AiServerExceptionEnum.VALIDATION_ERROR);
        }
        String batchNo = UuidUtils.getUUID();
        String taskId = null;
        try {
            DescribeDTO describeDTO = new DescribeDTO();
            describeDTO.setBase64("data:image/png;base64," + base64Img);
            describeDTO.setState(batchNo);
            describeDTO.setNotifyHook(properties.getNotifyHook());
            taskId = mjProxyWrapper.describe(describeDTO);
        } catch (AiServerException e){
            return Result.failed(e.getCode(), e.getMessage());
        }
        if (taskId == null) {
            return Result.failed(AiServerExceptionEnum.IMG_DESCRIBE_FAILED);
        }

        ConversationHistoryDO conversationHistoryDO = new ConversationHistoryDO();
        conversationHistoryDO.setUserId(userId);
        conversationHistoryDO.setShopType(ShopEnum.PICTURE.getType());
        conversationHistoryDO.setTaskId(taskId);
        conversationHistoryDO.setBatchNo(batchNo);
        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(new HistoryInfoDO(cosUtils.uploadImageFile(file, "/mj/" + DateUtils.getToday()), ImgStatus.IN_PROGRESS.getStatus(), Action.DESCRIBE.name())));
        conversationHistoryManager.insert(conversationHistoryDO);

        // 调用成功则对应次数减一
        if (userVipInfoDO.getSpeedRemainNumber() > 0) {
            userVipInfoDO.setSpeedRemainNumber(userVipInfoDO.getSpeedRemainNumber() - 1);
            userVipInfoManager.update(userVipInfoDO);
        }

        TaskDTO taskDTO = new TaskDTO(taskId, null);
        redisUtil.set(String.valueOf(conversationHistoryDO.getId()), JSON.toJSONString(taskDTO), 3600);
        return Result.success(conversationHistoryDO.getId());
    }

    private String getBase64(MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            return Base64.encodeBase64String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Result<Void> notify(TaskReq taskReq) {
        try {
            String batchNo = taskReq.getState();
            // 每次会话的批次号都不同，所有查询出来的结果有且仅有一个
            ConversationHistoryDO conversationHistoryDO = conversationHistoryManager.queryNewByBatchNoAndShopType(batchNo, ShopEnum.PICTURE.getType());
            if (conversationHistoryDO == null) {
                return Result.failed(AiServerExceptionEnum.VALIDATION_ERROR);
            }
            HistoryInfoDO historyInfoDO = JSON.parseObject(conversationHistoryDO.getHistoryInfo(), HistoryInfoDO.class);

            if (TaskStatus.FAILURE.equals(taskReq.getStatus())){
                failed(conversationHistoryDO, historyInfoDO);
            } else if (TaskStatus.SUCCESS.equals(taskReq.getStatus())){
                switch (taskReq.getAction()) {
                    case IMAGINE:
                    case UPSCALE:
                    case VARIATION:
                    case RESET:
                        if (StringUtils.isBlank(taskReq.getImageUrl())) {
                            failed(conversationHistoryDO, historyInfoDO);
                            return Result.success();
                        }
                        String imageUrl = taskReq.getImageUrl().replace("https://cdn.discordapp.com", "http://img.itbook123.com");
                        historyInfoDO.setA(uploadToCos(imageUrl));
                        historyInfoDO.setStatus(ImgStatus.SUCCESS.getStatus());
                        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(historyInfoDO));
                        conversationHistoryManager.update(conversationHistoryDO);
                        break;
                    case DESCRIBE:
                        if (StringUtils.isBlank(taskReq.getPrompt())) {
                            failed(conversationHistoryDO, historyInfoDO);
                            return Result.success();
                        }
                        historyInfoDO.setA(taskReq.getPrompt());
                        historyInfoDO.setStatus(ImgStatus.SUCCESS.getStatus());
                        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(historyInfoDO));
                        conversationHistoryManager.update(conversationHistoryDO);
                        break;
                }
            }
        }catch (Exception e){
            log.info("notify error req={}", JSON.toJSONString(taskReq), e);
        }
        return Result.success();
    }

    private String uploadToCos(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream is = new BufferedInputStream(uc.getInputStream());
            MultipartFile cMultiFile = new MockMultipartFile("image.jpg", is);
            return cosUtils.uploadImageFile(cMultiFile, "/mj/" + DateUtils.getToday());
        } catch (Exception e) {
            log.error("uploadToCos error", e);
            return null;
        }
    }

    private void failed(ConversationHistoryDO conversationHistoryDO, HistoryInfoDO historyInfoDO) {
        historyInfoDO.setStatus(ImgStatus.FAILURE.getStatus());
        historyInfoDO.setA("很抱歉，似乎有一些问题导致了您的绘图失败。如果该问题持续出现，请联系客服。");
        conversationHistoryDO.setHistoryInfo(JSON.toJSONString(historyInfoDO));
        conversationHistoryManager.update(conversationHistoryDO);
    }
}
