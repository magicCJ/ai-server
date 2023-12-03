package com.zhibai.ai.configuration;

import com.zhibai.ai.security.SseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/29 13:37
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

    @Resource
    SseInterceptor sseInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 拦截器
        registry.addInterceptor(sseInterceptor)
                .addPathPatterns("/scene/start/sse", "/scene/ask/sse");
        super.addInterceptors(registry);
    }


}