package com.zhibai.ai.model.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 1:50
 */

@Data
public class WxNoticeResource {

    /**
     * 加密算法类型
     */
    @NotBlank
    @Length(min=1, max=32)
    private String algorithm;

    /**
     * 数据密文
     */
    @NotBlank
    @Length(min=1, max=1048576)
    private String ciphertext;

    /**
     * 附加数据
     */
    @Length(min=1, max=16)
    private String associated_data;

    /**
     * 原始类型
     */
    @NotBlank
    @Length(min=1, max=16)
    private String original_type;

    /**
     * 随机串
     */
    @NotBlank
    @Length(min=1, max=16)
    private String nonce;

}
