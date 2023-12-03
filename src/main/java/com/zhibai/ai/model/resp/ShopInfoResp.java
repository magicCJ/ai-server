package com.zhibai.ai.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
    * 商品信息
    */
@Data
public class ShopInfoResp {

    private Long id;

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
    * 有效天数
    */
    private Integer effectiveDays;

    /**
    * 价格，单位：元
    */
    private BigDecimal price;

}