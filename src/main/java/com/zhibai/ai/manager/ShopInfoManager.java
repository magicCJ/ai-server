package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.ShopInfoDO;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:23
 * @description
 **/
public interface ShopInfoManager {

    List<ShopInfoDO> queryAll();

    ShopInfoDO queryById(Long id);

}
