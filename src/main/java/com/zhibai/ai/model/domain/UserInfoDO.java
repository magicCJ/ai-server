package com.zhibai.ai.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
    * 用户信息
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDO {
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
    * 昵称
    */
    private String nickName;

    /**
    * 普通用户的标识
    */
    private String openId;

    /**
    * 用户统一标识
    */
    private String unionId;

    /**
    * 头像照片
    */
    private String headImg;

    /**
    * 普通用户性别，1为男性，2为女性
    */
    private Integer sex;

    /**
    * 国家，如中国为CN
    */
    private String country;

    /**
    * 普通用户个人资料填写的省份
    */
    private String province;

    /**
    * 普通用户个人资料填写的城市
    */
    private String city;

    /**
     * 邀请人ID
     */
    private Long inviterId;

    /**
     * 提现余额
     */
    private BigDecimal withdrawalBalance;

    /**
    * 扩展信息
    */
    private HashMap<String, String> feature;

    public UserInfoDO(String nickName, String openId, String unionId, String headImg, Integer sex, String country, String province, String city) {
        this.nickName = nickName;
        this.openId = openId;
        this.unionId = unionId;
        this.headImg = headImg;
        this.sex = sex;
        this.country = country;
        this.province = province;
        this.city = city;
    }

}