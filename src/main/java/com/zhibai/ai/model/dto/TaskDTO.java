package com.zhibai.ai.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDTO implements Serializable {
	private static final long serialVersionUID = -674915748204390789L;

	private String id;
	private String prompt;

	public TaskDTO(String id, String prompt) {
		this.id = id;
		this.prompt = prompt;
	}



}

