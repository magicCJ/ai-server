package com.zhibai.ai.model.req;

import lombok.Data;

@Data
public class SubmitReq {

    private Long historyId;

    private String prompt;

    private Integer index;
}
