package com.zhibai.ai.listener;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamChatCompletionMessage {

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     *
     * <p><a href="https://platform.openai.com/docs/guides/safety-best-practices/end-user-ids">safety-best-practices</a>
     */
    @NotBlank
    @Builder.Default
    private String role = "user";

    /**
     * The input content.
     */
    @NotBlank
    private String content;

    /**
     * The name of the author of this message. May contain a-z, A-Z, 0-9, and underscores, with a maximum length of 64 characters.
     */
    private String name;
}
