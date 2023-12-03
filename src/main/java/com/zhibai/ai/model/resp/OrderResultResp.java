package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 23:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResultResp {

    /**
     * 订单id
     */
    @NotNull
    private Long orderId;

    /**
     * 用户id
     */
    @NotNull
    private Long userId;

    /**
     * 总次数
     */
    @NotNull
    private Integer total;

    /**
     * 已用次数
     */
    @NotNull
    private Integer used;

    /**
     * 剩余次
     */
    @NotNull
    private Integer available;

    /**
     * 用户级别
     */
    private Integer level;

    /**
     * 资源包有效天数
     */
    @NotNull
    private Integer day;

    /**
     * 活跃时长
     */
    private Integer activeTime;

    /**
     * 商品类别
     */
    private Integer productType;

    /**
     * 商品名称
     */
    @NotNull
    private String label;


    @NotNull
    private String createTime;

    @NotNull
    private String updateTime;

}
