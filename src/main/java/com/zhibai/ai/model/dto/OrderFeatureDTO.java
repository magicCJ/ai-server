package com.zhibai.ai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 2:31
 */
@Data
@AllArgsConstructor
public class OrderFeatureDTO {

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 交易类型
     */
    private String tradeType;


    /**
     * 交易状态描述
     */
    private String tradeStateDesc;


    /**
     * 付款银行
     */
    private String bankType;


    /**
     * 支付完成时间
     */
    private String successTime;


    /**
     * 支付者用户标识
     */
    private String payerOpenId;

}
