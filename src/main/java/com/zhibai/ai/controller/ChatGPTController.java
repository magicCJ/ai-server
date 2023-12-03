package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.SceneAskReq;
import com.zhibai.ai.model.req.SceneStartReq;
import com.zhibai.ai.model.resp.SceneInfoResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.ChatGPTService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 16:57
 * @description
 **/
@RestController
@RequestMapping("/scene")
public class ChatGPTController {

    @Resource
    private ChatGPTService chatGPTService;

    @GetMapping("/queryAll")
    public Result<List<SceneInfoResp>> queryAll(){
        return Result.success(chatGPTService.queryAll());
    }

    @PostMapping("/start")
    public void start(@RequestBody SceneStartReq req, HttpServletResponse response){
        req.setUserId(UserThreadLocal.getUserId());
        chatGPTService.start(req, response);
    }

    @PostMapping("/ask")
    public void ask(@RequestBody SceneAskReq req, HttpServletResponse response){
        req.setUserId(UserThreadLocal.getUserId());
        chatGPTService.ask(req, response);
    }

}
