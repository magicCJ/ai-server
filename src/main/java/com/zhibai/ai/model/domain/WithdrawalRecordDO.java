package com.zhibai.ai.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 提现记录
 * @TableName withdrawal_record
 */
@Data
public class WithdrawalRecordDO implements Serializable {
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
     * 提现金额，单位：元
     */
    private BigDecimal withdrawalAmount;

    /**
     * 提现方式，1：支付宝，2：微信
     */
    private Integer withdrawalType;

    /**
     * 提现时间
     */
    private LocalDateTime withdrawalTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 扩展信息
     */
    private String feature;

    private static final long serialVersionUID = 1L;
}