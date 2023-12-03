/*
 * Gongdao.com Inc.
 * Copyright (c) 2020-2034 All Rights Reserved.
 */
package com.zhibai.ai.enums;

/**
 * @Author xunbai
 * @Date 2023-05-14 23:41
 * @description
 **/
public enum GptErrorEnum {
    /**
     * 被封
     */
    account_deactivated("account_deactivated");

    private String code;

    GptErrorEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
