package com.zhibai.ai.model.resp;

import com.zhibai.ai.enums.Action;
import com.zhibai.ai.enums.TaskStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaskReq implements Serializable {
	private static final long serialVersionUID = -674915748204390789L;

	private Action action;
	private String id;
	private String prompt;
	private String promptEn;
	private String description;
	private String state;
	private Long submitTime;
	private Long finishTime;
	private String imageUrl;
	private TaskStatus status;

	public TaskReq() {
	}

	public TaskReq(String id, String prompt) {
		this.id = id;
		this.prompt = prompt;
	}



}

