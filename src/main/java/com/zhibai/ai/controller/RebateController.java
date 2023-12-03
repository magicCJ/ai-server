package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.RebateHomeResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.RebateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:58
 * @description
 **/
@RestController
@RequestMapping("/rebate")
public class RebateController {

    @Resource
    private RebateService rebateService;


    @GetMapping("/getInfo")
    public Result<RebateHomeResp> getInfo(){
        return rebateService.getInfo(UserThreadLocal.getUserId());
    }


}
