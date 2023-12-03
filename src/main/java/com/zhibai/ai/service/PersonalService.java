package com.zhibai.ai.service;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.PersonalInfoResp;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:35
 * @description
 **/
public interface PersonalService {

    Result<PersonalInfoResp> getPersonalInfo(Long userId);
}
