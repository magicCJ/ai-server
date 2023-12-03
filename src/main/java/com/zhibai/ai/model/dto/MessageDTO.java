package com.zhibai.ai.model.dto;

import lombok.Data;

@Data
public class MessageDTO<T> {
	private Integer code;
	private String description;
	private T result;

	public static final Integer SUCCESS_CODE = 1;

	public MessageDTO() {
	}

	private MessageDTO(int code, String description, T result) {
		this.code = code;
		this.description = description;
		this.result = result;
	}
}
