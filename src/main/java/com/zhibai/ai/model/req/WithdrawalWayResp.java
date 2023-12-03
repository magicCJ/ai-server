package com.zhibai.ai.model.req;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 提现方式
 * @TableName withdrawal_way
 */
@Data
public class WithdrawalWayResp implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 提现方式，1：支付宝，2：微信
     */
    private Integer withdrawalType;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 银行卡号
     */
    private String bankNumber;

    /**
     * 开户支行
     */
    private String bankName;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 微信账号
     */
    private String wechatAccount;

    private static final long serialVersionUID = 1L;
}