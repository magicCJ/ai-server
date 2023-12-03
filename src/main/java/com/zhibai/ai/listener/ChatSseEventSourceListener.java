package com.zhibai.ai.listener;

import com.alibaba.fastjson.JSON;
import com.lzhpo.chatgpt.sse.AbstractEventSourceListener;
import com.zhibai.ai.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/30 01:03
 */
@Slf4j
@Data
public class ChatSseEventSourceListener extends AbstractEventSourceListener {

    private final SseEmitter sseEmitter;

    private StringBuffer stringBuffer = new StringBuffer();

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ChatSseEventSourceListener(SseEmitter sseEmitter) {
        Assert.notNull(sseEmitter, "sseEmitter cannot null.");
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.debug("Execute onOpen method, response: {}", response);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        try {
            sseEmitter.send(data);
        } catch (IOException e) {
            log.error("error: {}, data: {}", StringUtil.getExceptionStackTrace(e), data);
        }
        // 记录数据
        if (data.equals("[DONE]")) {
            this.countDownLatch.countDown();
            log.debug("Execute onEvent method. id: {}, type: {}", id, type);
        } else {
            StreamChatCompletionResponse response = JSON.parseObject(data, StreamChatCompletionResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                return;
            }
            StreamChatCompletionMessage message;
            if ((message = response.getChoices().get(0).getDelta()) == null) {
                return;
            }
            if (message.getContent() != null) {
                stringBuffer.append(message.getContent());
            }
        }
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.debug("Execute onClosed method.");
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable e, @Nullable Response response) {
        String errorMsg = Optional.ofNullable(e)
                .map(Throwable::getMessage)
                .orElseGet(() -> Objects.nonNull(response) ? response.toString() : "Unexpected exception");
        log.error("Execute onFailure method, response: {}, error: {}", response, errorMsg);
        assert e != null;
        log.info(StringUtil.getExceptionStackTrace((Exception) e));
    }

}
