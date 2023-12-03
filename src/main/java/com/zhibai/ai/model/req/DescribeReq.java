package com.zhibai.ai.model.req;

import lombok.Data;

@Data
public class DescribeReq {
	/**
	 * 自定义参数.
	 */
	private String state;
	/**
	 * 文件base64: data:image/png;base64,xxx.
	 */
	private String base64;
	/**
	 * notifyHook of caller.
	 */
	private String notifyHook;
}
