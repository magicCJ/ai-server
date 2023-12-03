package com.zhibai.ai.model.req;

import lombok.Data;

/**
 * @Author xunbai
 * @Date 2023-05-13 20:16
 * @description
 **/
@Data
public class SceneAskReq extends BaseReq{

    private String batchNo;

    private Long sceneId;

    private String input;

}
