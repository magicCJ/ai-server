package com.zhibai.ai.controller;

import com.lzhpo.chatgpt.OpenAiClient;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.zhibai.ai.mapper.ConversationHistoryMapper;
import com.zhibai.ai.util.CosUtils;
import com.zhibai.ai.util.RedisUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CosUtils cosUtils;

    @Resource
    OpenAiClient openAiClient;


    @Resource
    private ConversationHistoryMapper conversationHistoryMapper;

    private static final String OPENAI_API_HOST = "https://api.openai.com/";

    private static final String LAST_CONTEXT = "LAST_CONTEXT";

    private static final Map<String, Integer> API_KEY_MAP = new LinkedHashMap<String, Integer>() {
        {
            put("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", 5);
        }
    };

    @GetMapping("/stream")
    public SseEmitter streamHandler(@RequestParam("input") String input) throws Exception {
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(50)
                .apiKey("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")
                .apiHost("https://service-pgn14xp7-1316905570.ca.apigw.tencentcs.com/")
                .build()
                .init();

        SseEmitter sseEmitter = new SseEmitter(-1L);

        GPTEventSourceListener listener = new GPTEventSourceListener(sseEmitter);
        Message message = Message.of("寫一個java簡易計算器");

        List<Message> messages = new ArrayList<>();
//        messages.add(message);
//        listener.setOnComplate(msg -> {
//            add(id, message);
//            add(id, Message.ofAssistant(msg));
//
//        });
        chatGPTStream.streamChatCompletion(messages, listener);

        return sseEmitter;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 创建 Redis 客户端实例
//        Jedis jedis = new Jedis("localhost", 6379);
////        jedis.auth("ai@2023");
//
//        // 将数据存储到 Redis 中
//        jedis.set("key1", "value1");
//
//        // 从 Redis 中获取数据
//        String value = jedis.get("key1");
//        System.out.println("key1：" + value);
//
//        // 关闭 Redis 客户端连接
//        jedis.close();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 864000 * 1000);

        String str = Jwts.builder()
                .setId(String.valueOf(1))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBbOROeXF6rBGB0OVeAn+SslKj/4IY3sTr14+2SVy+tryhokWu7tB1sYcVO8fzOo/9sWSMWzyukTf2p7o1y1//BGVzox2t0KUtnLjIxTdmmDcZtwLkS9cGgbEeiKUXD0OdQ9q9jmiMU7iQMsGZfRfj8kOp9In+8XpzW4jkwdU2/2OAG2SrRw63hdmyKcJY2Z0/UWdnOpa2PV6iVGsJju0qb5OnNZazNKgbL4zm/3DF/RJpKC7cXiKv0xsYDO+LlVLfBnTl1FA+XQz4wKQ/xCQ0BvPgWnAQJvWpLIIfaA8PBwgFs/DlYtMiDvlja7+6v+SwLAfa8ZOlfiaKjd7nAHVvAgMBAAECggEAT1ni16bOvSq2C/GtB0IzH/V0Hb+GmblaQ/6tAHVcTQkenZi6yadDceUgq79YqhoOVF9gBoyvelYtBU2OAN+sRCyDiksSs+nyuaGf1Q3DSp2LjUsdLpdGYW73QLWCNNGYRX4+0KEJlvgJsW6BKfy9vk/3HlQqAKMEPysAIAi89dBdQdriGaYW2Ve5FrukyuTIUiG7MAaT98F52Y3Zou9qxB3Oei7TWjWvXKa5uWRNCcTyYP3ATQjxMaeI3RBo6ttQC+NoCm/vyn/Atxh1qOKuv2yol4tbIkCr1d0eJz7INMo9qi5IUeWfU4skxdFT9PtxvRDf92s01MeGqrmdyytnUQKBgQDnlNGvw5+mHhZR/txOmu3KFo459R2PcvkxD1xz/v/iFvW+Adg5srJY3sN456u4+cj/qsnq9cyARsaHWRojOfiaT6Mg8YLeg5p32uggOYO8UA4iWKec+FmKhZDAO0owuGLrN/49AMDzkX16U3ATh6+k1wpAeUi0XzrmYtzx9/OGbQKBgQDV0hx6pWtXUDo+HMu7s59uKd3Of3Ir3GY7N+f1DaI96ELU50Lqnh8Any4lP+JA4+S8tM6lms1yjHZ9zV8FkfUhdWYlpHNR+ucJ+kfWCAnUvcuvf2VoJi6YkkxnTF7GBi23LyLawkdUWLqNAxb7pTvyc3j81Vsif2iP1IHJypIxywKBgGli0AmsfHqcNowZ7yfWWWAd45t8cHdXWw94bhpbcRbyW+Kb9EpNh9vphAsBYqRLB61Ri5HhSa8WhkIRylM9jmSq2RGk1mlsel8Qv5HqmoiX5da7gYtteVIQdk8ErD5/qoRgdtJMBYLSMsf4MIiytrvNIF021F9A9wzg8KssqyYBAoGBAKYkI8LcxAJNMWRP5bJMiOYFm/a3KpSQJdN/xjmwRU+LKFrb8y/wezB1f1qsP/ah3XsPlr4xAdAu+mcX6+ydE/bSjPNUy9J4fYtQMP9KsbDfZORRvg7mFDCzBLEDM2T1KhJ7ir6mVNxiMVoee34/Tsw+E1WJxs2QaEPSIKMWRvBvAoGAGYHj7woCT04FfLUkbfLcOfbtCF+FrrtlQdKnAH8bKrg7EcpUQK1iNacBILfqwyhG5rE9iG2K7hEuRvNp9Wq6LDWYJYy5WwDTbyfG/tBgm86CH0zlEVr6Z4GbOvFvSi44STVwcsI2qSZA4cS4ZBhKLatjRS8JQMxxiPu73t4m/f4=")
                .compact();
        System.out.println(str);
    }
}
