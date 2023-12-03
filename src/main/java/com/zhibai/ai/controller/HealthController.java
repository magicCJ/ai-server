package com.zhibai.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class HealthController {

    /**
     * 服务探活接口
     * @return
     */
    @GetMapping("/checkHealth")
    public String checkHealth() {
        return "success";
    }
}
