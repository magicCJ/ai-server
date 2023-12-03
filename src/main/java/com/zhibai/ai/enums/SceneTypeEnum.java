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
public enum SceneTypeEnum {

    OCCUPATION(1, "职业"),
    THESIS(2, "论文"),
    PROGRAMMING(3, "编程"),
    COPYWRITING(4, "文案"),
    WRITING(5, "写作"),
    ENTERPRISE(6, "企业"),
    EMOTION(7, "情感"),
    STORY(8, "故事"),
    TRANSLATION(9, "翻译"),
    EDUCATION(10, "教育"),
    HEALTH(11, "健康"),
    GAMING(12, "游戏"),
    DRAWING(13, "绘画");

    private Integer type;

    private String desc;

    public static String getDescByType(Integer type){
        for (SceneTypeEnum value : values()) {
            if (value.type.equals(type)){
                return value.desc;
            }
        }
        return "";
    }
}
