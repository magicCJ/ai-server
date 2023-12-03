package com.zhibai.ai.model.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录
 * @TableName withdrawal_record
 */
@Data
public class WithdrawalRecordReq implements Serializable {

    private Long userId;

    private Integer currentPage;

    private Integer pageSize;

    private static final long serialVersionUID = 1L;
}