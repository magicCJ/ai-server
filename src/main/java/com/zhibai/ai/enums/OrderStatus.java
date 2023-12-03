package com.zhibai.ai.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

	WAITING_PAY(0, "待支付"),
	FINISHED_PAY(1, "支付完成"),
	CANCEL_PAY(2, "支付已取消"),
	REFUNDING(3, "退款处理中"),
	FINISHED_REFUND(4, "退款已完成");

	private Integer code;

	private String desc;

}
