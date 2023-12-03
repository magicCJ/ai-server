package com.zhibai.ai.model.resp;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:44
 * @description
 **/
@Data
public class VipInfoResp {

    private Integer type;

    private String vipTitle;

    private String totalNumber;

    /**
     * 文字剩余次数, -99表示不限次数
     */
    private String textRemainNumber;

    /**
     * 极速模式剩余次数, -99表示不限次数
     */
    private String speedRemainNumber;

    /**
     * 放松模式剩余次数, -99表示不限次数
     */
    private String relaxRemainNumber;

    /**
     * 过期时间
     */
    private String expiredTime;
}
