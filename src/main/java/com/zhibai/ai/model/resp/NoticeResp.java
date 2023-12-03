package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 3:09
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResp {

    @NotNull
    private String code;

    @NotBlank
    private String message;
}
