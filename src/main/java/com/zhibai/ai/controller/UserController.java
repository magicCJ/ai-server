package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.resp.QRLoginResp;
import com.zhibai.ai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: kanson
 * @desc: 用户相关
 * @version: 1.0
 * @time: 2023/05/26 21:10
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;

    /**
     * pc端调用微信登录，生成跳转链接
     *
     * @param inviterId 邀请人id
     * @return
     */
    @GetMapping("/login/pc")
    public Result<QRLoginResp> preLogin(@RequestParam(value = "inviter", required = false, defaultValue = "0") Long inviterId,
                                        HttpServletRequest request) {
        QRLoginResp resp = userService.preLogin(inviterId, request);
        return Result.success(resp);
    }

    /**
     * 微信登录接口
     *
     * @param code    登录凭证
     * @param key     唯一key
     * @param inviterId 邀请人id
     * @return 登录后有效身份凭证token，对应用户信息缓存，此后请求时作为Header参数中的"Token"，校验请求登录有效性，快速获取用户信息
     */
    @GetMapping("/login/result")
    public String login(@RequestParam(value = "code", required = false) String code,
                        @RequestParam(value = "key", required = false) String key,
                        @RequestParam(value = "inviter", required = false) Long inviterId) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return userService.login(code, inviterId, key);
    }

    @GetMapping("/login/polling")
    public String polling(@RequestParam("key") String key) {
        return userService.polling(key);
    }

    /**
     * 用于同微信应用账号做服务接入验签，与页面交互无关
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return
     */
    @GetMapping("/login/verify")
    public String verify(@RequestParam("signature") String signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("echostr") String echostr) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("signature", signature);
        params.put("timestamp", timestamp);
        params.put("nonce", nonce);
        params.put("echostr", echostr);
        return userService.verify(params);
    }


}
