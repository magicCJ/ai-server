package com.zhibai.ai.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImgStatus {
	/**
	 * 执行中.
	 */
	IN_PROGRESS("生成中"),
	/**
	 * 失败.
	 */
	FAILURE("失败"),
	/**
	 * 成功.
	 */
	SUCCESS("已完成");

	private String status;

}
