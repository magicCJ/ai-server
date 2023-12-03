package com.zhibai.ai.wrapper;

import com.alibaba.fastjson.JSON;
import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.configuration.ProxyProperties;
import com.zhibai.ai.model.dto.DescribeDTO;
import com.zhibai.ai.model.dto.MessageDTO;
import com.zhibai.ai.model.dto.SubmitDTO;
import com.zhibai.ai.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author xunbai
 * @Date 2023-05-20 14:16
 * @description
 **/
@Slf4j
@Service
public class MjProxyWrapper {

    @Resource
    private ProxyProperties properties;

    public String submit(SubmitDTO submitDTO) {
        try {
            String result = OkHttpUtils.post(properties.getMjProxyUrl() + "/trigger/submit", JSON.toJSONString(submitDTO));
            if (StringUtils.isBlank(result)) {
                return null;
            }
            MessageDTO<String> messageDTO = JSON.parseObject(result, MessageDTO.class);
            if (MessageDTO.SUCCESS_CODE.equals(messageDTO.getCode())) {
                return messageDTO.getResult();
            }
        } catch (Exception e){
            log.error("MjProxyWrapper#submit#error", e);
        }
        return null;
    }

    public String describe(DescribeDTO describeDTO) {
        try {
            String result = OkHttpUtils.post(properties.getMjProxyUrl() + "/trigger/describe", JSON.toJSONString(describeDTO));
            if (StringUtils.isBlank(result)) {
                return null;
            }
            if (result.contains("Request Entity Too Large")){
                throw new AiServerException(AiServerExceptionEnum.IMG_TOO_LARGE);
            }
            MessageDTO<String> messageDTO = JSON.parseObject(result, MessageDTO.class);
            if (MessageDTO.SUCCESS_CODE.equals(messageDTO.getCode())) {
                return messageDTO.getResult();
            }
        } catch (AiServerException e){
            throw e;
        } catch (Exception e){
            log.error("MjProxyWrapper#submit#error", e);
        }
        return null;
    }


}
