package com.zhibai.ai.model.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
    * 用户会员信息
    */
@Data
public class UserVipInfoDO {
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
    * 类型，1-文字会员，2-画图会员
    */
    private Integer type;

    /**
    * 套餐类型，1-体验卡，2-月度，3-季度，4-年度
    */
    private Integer packageType;

    /**
    * 总次数, -99表示不限次数
    */
    private Integer totalNumber;

    /**
    * 文字剩余次数, -99表示不限次数
    */
    private Integer textRemainNumber;

    /**
    * 极速模式剩余次数, -99表示不限次数
    */
    private Integer speedRemainNumber;

    /**
    * 放松模式剩余次数, -99表示不限次数
    */
    private Integer relaxRemainNumber;

    /**
    * 过期时间
    */
    private LocalDateTime expiredTime;

    /**
    * 扩展信息
    */
    private HashMap<String, String> feature;
}