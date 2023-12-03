package com.zhibai.ai.model.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
    * 场景提示词信息
    */
@Data
public class SceneInfoDO {
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
     * 状态，0-不可用，1-可用
     */
    private Integer status;

    /**
    * 场景类型，如：情感、职业等
    */
    private Integer sceneType;

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
    * 场景提示词
    */
    private String scenePrompt;

    /**
    * 填充信息
    */
    private List<String> fillInfo;

    /**
    * 扩展信息
    */
    private HashMap<String, String> feature;
}