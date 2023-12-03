package com.zhibai.ai.manager.impl;

import com.zhibai.ai.manager.ShopInfoManager;
import com.zhibai.ai.mapper.ShopInfoMapper;
import com.zhibai.ai.model.domain.ShopInfoDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:23
 * @description
 **/
@Service
public class ShopInfoManagerImpl implements ShopInfoManager {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public List<ShopInfoDO> queryAll() {
        return shopInfoMapper.queryAll();
    }

    @Override
    public ShopInfoDO queryById(Long id) {
        return shopInfoMapper.queryById(id);
    }
}
