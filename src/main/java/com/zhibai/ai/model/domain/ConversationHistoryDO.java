package com.zhibai.ai.model.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
* 提示词信息
*/
@Data
public class ConversationHistoryDO {
    private Long id;

    /**
    * 创建时间
    */
    private LocalDateTime gmtCreate;

    /**
    * 修改时间
    */
    private LocalDateTime gmtModified;

    /**
    * 版本
    */
    private Integer version;

    /**
    * 用户ID
    */
    private Long userId;

    /**
     * 商品类型
     */
    private Integer shopType;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 任务ID
     */
    private String taskId;

    /**
    *  历史聊天信息
    */
    private String historyInfo;

    /**
    * 扩展信息
    */
    private HashMap<String, String> feature;

}