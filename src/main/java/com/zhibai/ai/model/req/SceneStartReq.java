package com.zhibai.ai.model.req;

import lombok.Data;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 20:16
 * @description
 **/
@Data
public class SceneStartReq extends BaseReq{

    private Long sceneId;

    private List<String> fillInfo;

}
