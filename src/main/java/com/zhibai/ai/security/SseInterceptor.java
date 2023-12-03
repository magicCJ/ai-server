package com.zhibai.ai.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class SseInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        log.info("请求开始:{},token:{}", request.getRequestURI(), request.getHeader("token"));

        boolean flag = false;
        if (handler instanceof HandlerMethod) {
            try {
                String token = request.getHeader("token");
                if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
                    Long userId = tokenProvider.getUserIdFromToken(token);
                    UserThreadLocal.setUserId(userId);
                    flag = true;
                } else {
                    log.info("从redis里获取token相关的结果失败，返回401");
                    response.sendError(401, "Redis里未获取到相关数据，Token 已失效！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("KudosLoginInterceptor preHandle err.\n" + e.getMessage());
            }
        }
        return flag;
    }

}
