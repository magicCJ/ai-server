package com.zhibai.ai.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 提现方式
 * @TableName withdrawal_way
 */
@Data
public class WithdrawalWayDO implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 用户ID
     */
    private Long userId;

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

    /**
     * 扩展信息
     */
    private String feature;

    private static final long serialVersionUID = 1L;
}