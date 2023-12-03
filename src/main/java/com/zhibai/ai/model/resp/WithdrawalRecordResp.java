package com.zhibai.ai.model.resp;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录
 * @TableName withdrawal_record
 */
@Data
public class WithdrawalRecordResp implements Serializable {

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

    private static final long serialVersionUID = 1L;
}