/*
 * Gongdao.com Inc.
 * Copyright (c) 2020-2034 All Rights Reserved.
 */
package com.zhibai.ai.model.gptModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;


 /**
 * @Author xunbai
 * @Date 2023-05-13 17:01
 * @description
 * 流式正确返回：
 * {
 *     "id":"chatcmpl-7Fk1Z0Pe6pi9MZ5taI1sbhWrdWubQ",
 *     "object":"chat.completion.chunk",
 *     "created":1683986193,
 *     "model":"gpt-3.5-turbo-0301",
 *     "choices":[
 *         {
 *             "delta":{
 *                 "content":"则"
 *             },
 *             "index":0,
 *             "finish_reason":null
 *         }
 *     ]
 * }
 */
@Data
public class GptStreamResp {

    private String id;

    private String object;

    private Date created;

    private String model;

    private List<Choice> choices;

    private Error error;

    @Data
    public static final class Choice{
        private Delta delta;
        private Integer index;

        @JSONField(name = "finish_reason")
        private String finishReason;
    }

    @Data
    public static final class Delta{
        private String content;
    }

    @Data
    public static final class Error{
        private String message;

        private String type;

        //private JSONOb param;

        private String code;
    }
}
