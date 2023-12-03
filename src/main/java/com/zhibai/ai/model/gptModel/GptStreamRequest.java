package com.zhibai.ai.model.gptModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 23:56
 * @description
 **/
@Data
public class GptStreamRequest {

    private Double temperature;

    private List<Message> messages;

    private String model;

    @JSONField(name = "max_tokens")
    private Integer maxToken;

    private Boolean stream;


    @Data
    @AllArgsConstructor
    public static final class Message{
        private String role;
        private String content;
    }
}
