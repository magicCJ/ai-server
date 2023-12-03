package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.PersonalInfoResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.PersonalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:34
 * @description
 **/
@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Resource
    private PersonalService personalService;

    @GetMapping("/query")
    public Result<PersonalInfoResp> getPersonalInfo(){
        return personalService.getPersonalInfo(UserThreadLocal.getUserId());
    }
}
