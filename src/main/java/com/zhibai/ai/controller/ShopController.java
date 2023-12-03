package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.ShopInfoResp;
import com.zhibai.ai.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:28
 * @description
 **/

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    @GetMapping("/queryAll")
    public Result<List<ShopInfoResp>> queryAll(){
        return shopService.queryAll();
    }
}
