package com.zhibai.ai.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:16
 * @description
 **/
@Data
public class SceneInfoResp {

    private Long id;

    /**
     * 场景类型，如：情感、职业等
     */
    private String sceneType;

    /**
     * 场景ICON
     */
    private String sceneIcon;

    /**
     * 场景标题
     */
    private String sceneTitle;

    /**
     * 场景描述
     */
    private String sceneDesc;

    /**
     * 填充信息
     */
    private List<String> fillInfo;
}
