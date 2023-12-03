package com.zhibai.ai.util;

import com.alibaba.fastjson.JSON;
import com.zhibai.ai.model.dto.WeixinUserInfo;
import com.zhibai.ai.model.dto.WeixinValidateInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.StringJoiner;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/26 22:26
 */
@Component
@Slf4j
public class WeixinUtil {

    @Value("${weixin.appId}")
    String appId;

    @Value("${weixin.appSecret}")
    String appSecret;

    @Value("${weixin.token}")
    String token;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 验证签名是否正确
     *
     * @param params 待验证参数
     * @return 验证结果
     */
    public String verifySign(SortedMap<String, String> params) {
        log.info("params: {}", params);
        String signature = params.get("signature");
        String echostr = params.get("echostr");
        params.remove("signature");
        params.remove("echostr");
        params.put("token", token);
        StringJoiner sj = new StringJoiner("");
        List<String> list = new ArrayList<>();
        params.forEach((key, value) -> list.add(value));
        list.stream().sorted().forEach(sj::add);
        String result = sj.toString();
        String calculatedSign = StringUtil.sha1Encrypt(result);
        log.info("result: {}, calculatedSign: {}, signature: {}", result, calculatedSign, signature);
        if (signature.equals(calculatedSign)) {
            return echostr;
        } else {
            return "";
        }
    }

    public WeixinValidateInfo validateLoginInfo(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        String result;
        try {
            result = OkHttpUtils.get(url);
        } catch (Exception e) {
            log.error(StringUtil.getExceptionStackTrace(e));
            return null;
        }
        log.info("result: {}", result);
        try {
            return JSON.parseObject(result, WeixinValidateInfo.class);
        } catch (Exception e) {
            log.info(result);
            log.error(StringUtil.getExceptionStackTrace(e));
            return null;
        }
    }

    public WeixinUserInfo getUserInfo(String accessToken, String openid) {
        // 获取用户信息
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
        String result;
        try {
            result = OkHttpUtils.get(url);
        } catch (Exception e) {
            log.error(StringUtil.getExceptionStackTrace(e));
            return null;
        }
        log.info("result: {}", result);
        try {
            return JSON.parseObject(result, WeixinUserInfo.class);
        } catch (Exception e) {
            log.info(result);
            log.error(StringUtil.getExceptionStackTrace(e));
            return null;
        }
    }

    public String setValidateInfoToRedis(WeixinValidateInfo validateInfo, int startTime, String ticket) {
        String key = DigestUtils.md5DigestAsHex(validateInfo.getAccess_token().getBytes());
        int duration = (int) (System.currentTimeMillis() / 1000) - startTime;
        int seconds = validateInfo.getExpires_in() - duration;
        redisUtil.set(key, JSON.toJSONString(validateInfo), seconds);
        log.info("set key:{}, seconds: {}", key, seconds);
        if (StringUtils.isNotEmpty(ticket)) {
            redisUtil.set(ticket, key, seconds);
        }
        return key;
    }

}
