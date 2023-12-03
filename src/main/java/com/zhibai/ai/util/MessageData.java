package com.zhibai.ai.util;

import com.zhibai.ai.enums.Action;
import lombok.Data;

@Data
public class MessageData {
	private Action action;
	private String prompt;
	private int index;
	private String status;
}
