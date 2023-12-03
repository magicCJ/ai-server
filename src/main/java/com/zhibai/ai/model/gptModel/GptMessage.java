package com.zhibai.ai.model.gptModel;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @Author xunbai
 * @Date 2023-05-13 22:56
 * @description
 **/
@ToString
@AllArgsConstructor
public class GptMessage {
    private String role;
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
