package com.zhibai.ai.listener;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/30 02:59
 */
@Data
public class StreamChatCompletionChoice {

    private Long index;

    private StreamChatCompletionMessage delta;

    @JsonProperty("finish_reason")
    private String finishReason;
}
