package com.zhibai.ai.service;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.ShopInfoResp;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:25
 * @description
 **/
public interface ShopService {

    Result<List<ShopInfoResp>> queryAll();
}
