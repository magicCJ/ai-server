package com.zhibai.ai.manager.impl;

import com.zhibai.ai.manager.SceneInfoManager;
import com.zhibai.ai.mapper.SceneInfoMapper;
import com.zhibai.ai.model.domain.SceneInfoDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:27
 * @description
 **/
@Service
public class SceneInfoManagerImpl implements SceneInfoManager {

    @Resource
    private SceneInfoMapper sceneInfoMapper;

    @Override
    public SceneInfoDO queryByUserId(Long id) {
        AssertUtil.isNotNull(id, "id");
        return sceneInfoMapper.queryById(id);
    }

    @Override
    public List<SceneInfoDO> queryAll() {
        return sceneInfoMapper.queryAll();
    }
}
