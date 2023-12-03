package com.zhibai.ai.service.impl;

import com.zhibai.ai.manager.ShopInfoManager;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.ShopInfoDO;
import com.zhibai.ai.model.resp.ShopInfoResp;
import com.zhibai.ai.service.ShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:26
 * @description
 **/

@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopInfoManager shopInfoManager;

    @Override
    public Result<List<ShopInfoResp>> queryAll() {
        List<ShopInfoDO> shopInfoList = shopInfoManager.queryAll();

        List<ShopInfoResp> result = new ArrayList<>();
        shopInfoList.forEach(shopInfoDO -> {
            ShopInfoResp shopInfoResp = new ShopInfoResp();
            BeanUtils.copyProperties(shopInfoDO, shopInfoResp);
            result.add(shopInfoResp);
        });

        return Result.success(result);
    }
}
