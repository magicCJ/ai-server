package com.zhibai.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author xunbai
 * @Date 2023-05-13 23:41
 * @description
 **/
@Getter
@AllArgsConstructor
public enum ShopEnum {

    TEXT(1, "文字(3.5)"),
    PICTURE(2," 画图"),
    GPT4(3, "4.0");

    private Integer type;

    private String desc;

    public static String getDescByType(Integer type){
        for (ShopEnum value : values()) {
            if (value.type.equals(type)){
                return value.desc;
            }
        }
        return "";
    }

    public static ShopEnum getByType(Integer type){
        for (ShopEnum value : values()) {
            if (value.type.equals(type)){
                return value;
            }
        }
        return null;
    }
}
