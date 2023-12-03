package com.zhibai.ai.model.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 会话标题历史信息
 * @TableName conversation_title
 */
@Data
public class HistoryTitleResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 历史标题
     */
    private String historyTitle;
}