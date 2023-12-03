package com.zhibai.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:41
 * @description
 **/

@Getter
@AllArgsConstructor
public enum PackageTypeEnum {

    PICTURE(1,"新人体验卡"),
    MEMBERSHIP(2, "会员");

    private Integer type;

    private String desc;

    public static String getDescByType(Integer type){
        for (PackageTypeEnum value : values()) {
            if (value.type.equals(type)){
                return value.desc;
            }
        }
        return "";
    }
}
