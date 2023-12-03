package com.zhibai.ai.model.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
    * 商品信息
    */
@Data
public class ShopInfoDO {
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
    * 商品类型，1-文字会员，2-画图会员
    */
    private Integer type;

    /**
    * 商品名称
    */
    private String shopTitle;

    /**
    * 商品描述
    */
    private String shopDescribe;

    /**
     * 数量
     */
    private Integer number;

    /**
    * 有效天数
    */
    private Integer effectiveDays;

    /**
    * 价格，单位：元
    */
    private BigDecimal price;

    /**
    * 扩展信息
    */
    private HashMap<String, String> feature;
}