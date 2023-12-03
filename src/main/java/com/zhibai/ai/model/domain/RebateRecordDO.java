package com.zhibai.ai.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 返利记录
 * @TableName rebate_record
 */
@Data
public class RebateRecordDO implements Serializable {
    /**
     * 
     */
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
     * 邀请用户ID
     */
    private Long inviterId;

    /**
     * 邀请用户昵称
     */
    private Long inviterName;

    /**
     * 商品类型，1-文字会员，2-画图会员，3-GPT4.0会员
     */
    private Integer shopType;

    /**
     * 商品名称
     */
    private String shopTitle;

    /**
     * 价格，单位：元
     */
    private BigDecimal price;

    /**
     * 返利级别
     */
    private Integer rebateLevel;

    /**
     * 返利比例
     */
    private BigDecimal rebateRatio;

    /**
     * 返利金额
     */
    private BigDecimal rebateAmount;

    /**
     * 扩展信息
     */
    private String feature;

    private static final long serialVersionUID = 1L;
}