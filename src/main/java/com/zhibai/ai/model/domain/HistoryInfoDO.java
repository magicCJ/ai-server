package com.zhibai.ai.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xunbai
 * @Date 2023-05-14 16:58
 * @description
 **/
@Data
public class HistoryInfoDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String q;

    private String a;

    private String status;

    private String action;

    public HistoryInfoDO(String q, String a) {
        this.q = q;
        this.a = a;
    }

    public HistoryInfoDO(String q, String status, String action) {
        this.q = q;
        this.status = status;
        this.action = action;
    }

    public HistoryInfoDO(String q, String a, String status, String action) {
        this.q = q;
        this.a = a;
        this.status = status;
        this.action = action;
    }
}
