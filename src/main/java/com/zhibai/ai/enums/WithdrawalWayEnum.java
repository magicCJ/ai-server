package com.zhibai.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author xunbai
 * @Date 2023-05-27 23:04
 * @description
 **/
@Getter
@AllArgsConstructor
public enum WithdrawalWayEnum {

    ALIPAY(1,"支付宝"),
    WECHAT(2,"微信"),
    BLANK(3,"银行卡"),
    ;


    private Integer way;

    private String desc;

}
