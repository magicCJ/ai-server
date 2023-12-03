package com.zhibai.ai.controller;

import com.lzhpo.chatgpt.OpenAiClient;
import com.zhibai.ai.model.req.SceneAskReq;
import com.zhibai.ai.model.req.SceneStartReq;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/29 23:29
 */
@Controller
@RequestMapping("/scene")
@RequiredArgsConstructor
public class StreamController {

    @Resource
    private ChatGPTService chatGPTService;

    private final OpenAiClient openAiClient;

    @ResponseBody
    @PostMapping("/start/sse")
    public SseEmitter startSse(@RequestBody SceneStartReq req){
        return chatGPTService.startSse(req, openAiClient);
    }

    @ResponseBody
    @PostMapping("/ask/sse")
    public SseEmitter askSse(@RequestBody SceneAskReq req){
        req.setUserId(UserThreadLocal.getUserId());
        return chatGPTService.askSse(req, openAiClient);
    }

}
