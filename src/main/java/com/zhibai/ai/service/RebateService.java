package com.zhibai.ai.service;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.RebateHomeResp;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:12
 * @description
 **/
public interface RebateService {

    Result<RebateHomeResp> getInfo(Long userId);
}
