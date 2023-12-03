package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.SceneInfoDO;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:27
 * @description
 **/
public interface SceneInfoManager {

    SceneInfoDO queryByUserId(Long id);

    List<SceneInfoDO> queryAll();
}
