package com.zhibai.ai.service;

import com.lzhpo.chatgpt.OpenAiClient;
import com.zhibai.ai.model.req.SceneAskReq;
import com.zhibai.ai.model.req.SceneStartReq;
import com.zhibai.ai.model.resp.SceneInfoResp;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 16:57
 * @description
 **/
public interface ChatGPTService {

    List<SceneInfoResp> queryAll();

    // 生成注释

    /**
     * 开始对话
     * @param req
     * @param response
     */
    void start(SceneStartReq req, HttpServletResponse response);

    boolean ask(SceneAskReq req, HttpServletResponse response);

    SseEmitter startSse(SceneStartReq req, OpenAiClient openAiClient);

    SseEmitter askSse(SceneAskReq req, OpenAiClient openAiClient);
}
