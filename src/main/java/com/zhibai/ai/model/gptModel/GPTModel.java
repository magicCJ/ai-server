/*
 * Gongdao.com Inc.
 * Copyright (c) 2020-2034 All Rights Reserved.
 */
package com.zhibai.ai.model.gptModel;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 瑜登
 * @version 2023年04月02日 11:07 AM
 *
 * {
 *  'id': 'chatcmpl-6p9XYPYSTTRi0xEviKjjilqrWU2Ve',
 *  'object': 'chat.completion',
 *  'created': 1677649420,
 *  'model': 'gpt-3.5-turbo',
 *  'usage': {'prompt_tokens': 56, 'completion_tokens': 31, 'total_tokens': 87},
 *  'choices': [
 *    {
 *     'message': {
 *       'role': 'assistant',
 *       'content': 'The 2020 World Series was played in Arlington, Texas at the Globe Life Field, which was the new
 *       home stadium for the Texas Rangers.'},
 *     'finish_reason': 'stop',
 *     'index': 0
 *    }
 *   ]
 * }
 *
 * 错误情况
 * {
 *   "error": {
 *     "message": "This key is associated with a deactivated account. If you feel this is an error, contact us
 *     through our help center at help.openai.com.",
 *     "type": "invalid_request_error",
 *     "param": null,
 *     "code": "account_deactivated"
 *   }
 */
@Data
public class GPTModel {

    private String id;

    private String object;

    private Date created;

    private String model;

    private Usage usage;

    private List<Choice> choices;

    private Error error;

    @Data
    public static final class Usage{
        private Integer prompt_tokens;

        private Integer completion_tokens;

        private Integer total_tokens;
    }

    @Data
    public static final class Message{
        private String role;

        private String content;

        private String finish_reason;

        private Integer index;
    }

    @Data
    public static final class Choice{
        private Message message;
    }

    @Data
    public static final class Error{
        private String message;

        private String type;

        //private JSONOb param;

        private String code;
    }
}
