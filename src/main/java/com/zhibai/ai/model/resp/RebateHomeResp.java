package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:06
 * @description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebateHomeResp {

    /**
     * 邀请链接
     */
    private String invitationUrl;

    /**
     * 总返利金额
     */
    private BigDecimal totalRebateAmount;

    /**
     * 可提现余额
     */
    private BigDecimal withdrawalBalance;

    /**
     * 邀请人数
     */
    private Integer invitationPeopleNumber;

    /**
     * 邀请人购买量
     */
    private Integer invitationOrderNumber;
}
