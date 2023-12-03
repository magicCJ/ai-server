package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 10:53
 */
@Data
@AllArgsConstructor
public class QRLoginResp {

    /**
     * 请求唯一key
     */
    @NotNull
    private String key;

    /**
     * 扫码登录时跳转用服务端地址
     */
    @NotNull
    private String loginQRUrl;

}
