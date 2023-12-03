package com.zhibai.ai.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class ProxyProperties {

    private String mjProxyUrl;

    private String notifyHook;

    /**
     * openai配置.
     */
    private final OpenaiConfig openai = new OpenaiConfig();

    @Data
    public static class OpenaiConfig {
        /**
         * gpt的api-key.
         */
        private String gptApiKey;

        /**
         * gpt请求的代理地址
         */
        private String gptProxyUrl;
        /**
         * 超时时间.
         */
        private Duration timeout = Duration.ofSeconds(30);
        /**
         * 使用的模型.
         */
        private String model = "gpt-3.5-turbo";
        /**
         * 返回结果的最大分词数.
         */
        private int maxTokens = 3000;
        /**
         * 相似度，取值 0-2.
         */
        private double temperature = 0.6;
    }
}
