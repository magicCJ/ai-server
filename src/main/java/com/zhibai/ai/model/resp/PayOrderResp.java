package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 23:21
 */
@Data
@AllArgsConstructor
public class PayOrderResp {

    /**
     * 订单id
     */
    @NotNull
    private Long orderNo;

    /**
     * 扫码支付时跳转用服务端地址
     */
    @NotNull
    private String qrCode;

}
