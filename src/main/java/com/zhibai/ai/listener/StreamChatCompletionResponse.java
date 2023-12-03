package com.zhibai.ai.listener;

import lombok.Data;

import java.util.List;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/30 02:57
 */
@Data
public class StreamChatCompletionResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    //private CommonUsage usage;
    private List<StreamChatCompletionChoice> choices;
}
