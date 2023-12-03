package com.zhibai.ai.translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhibai.ai.configuration.ProxyProperties;
import com.zhibai.ai.enums.GptErrorEnum;
import com.zhibai.ai.model.gptModel.GPTModel;
import com.zhibai.ai.model.gptModel.GptMessage;
import com.zhibai.ai.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GPTTranslate {
    @Resource
    private ProxyProperties properties;

    public String translateToEnglish(String prompt) {
        if (!containsChinese(prompt)) {
            return prompt;
        }
        return gptComplete(prompt);
    }

    private boolean containsChinese(String prompt) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(prompt);
        return m.find();
    }

    private String gptComplete(String prompt) {
        Map<String, String> header = Maps.newHashMap();

        String drawUrl = properties.getOpenai().getGptProxyUrl() + "/v1/chat/completions";

        String cookie = "";
        header.put("Authorization", "Bearer " + properties.getOpenai().getGptApiKey());
        Map<String, Object> body = Maps.newHashMap();
        body.put("model", properties.getOpenai().getModel());
        body.put("messages", Lists.newArrayList(new GptMessage("system", "把中文翻译成英文"), new GptMessage("user", prompt)));
        body.put("max_tokens", properties.getOpenai().getMaxTokens());
        body.put("temperature", properties.getOpenai().getTemperature());
        MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON1, JSON.toJSONString(body));

        String response = null;
        try {
            response = OkHttpUtils.post(drawUrl, cookie, requestBody, header);
        } catch (Exception e) {
            return null;
        }

        if (StringUtils.isBlank(response)) {
            return null;
        }

        GPTModel gptModel = JSONObject.parseObject(response, GPTModel.class);
        if (gptModel == null){
            return null;
        }

        //被封场景
        if (gptModel.getError() != null && GptErrorEnum.account_deactivated.getCode().equals(gptModel.getError().getCode())){
            return null;
        }

        return gptModel.getChoices().get(0).getMessage().getContent();
    }
}