package com.zhibai.ai.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.zhibai.ai.model.req.WxPayNoticeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc: 微信支付
 * @version: 1.0
 * @time: 2023/05/27 22:46
 */
@Component
@Slf4j
public class WxPayUtil {

    /**
     * 商户号
     */
    @Value("${wxpay.merchantId}")
    String merchantId;

    /**
     * 商户号关联的appId
     */
    @Value("${wxpay.appId}")
    String appId;

    /**
     * 商户证书序列号
     */
    @Value("${wxpay.merchantSerialNumber}")
    String merchantSerialNumber;

    /**
     * 商户API私钥路径
     */
    @Value("${wxpay.privateKeyPath}")
    String privateKeyPath;

    /**
     * 商户APIV3密钥
     */
    @Value("${wxpay.apiV3key}")
    String apiV3key;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AesUtil aesUtil;

    NativePayService service;

    @PostConstruct
    public void init() {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
//        log.info("privateKeyPath: {}", privateKeyPath);
//        Config config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(merchantId)
//                .privateKeyFromPath(privateKeyPath)
//                .merchantSerialNumber(merchantSerialNumber)
//                .apiV3Key(apiV3key)
//                .build();
//        // 构建service
//        service = new NativePayService.Builder().config(config).build();
    }

    public String getPayUrl(PrepayRequest request) {
        request.setAppid(appId);
        request.setMchid(merchantId);
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        return response.getCodeUrl();
    }

    public JSONObject decryptNotice(WxPayNoticeReq req) {
        try {
            String data = aesUtil.decryptToString(req.getResource().getAssociated_data().getBytes(),
                    req.getResource().getNonce().getBytes(), req.getResource().getCiphertext());
            return JSON.parseObject(data);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction getTransaction(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(merchantId);
        request.setOutTradeNo(outTradeNo);
        return service.queryOrderByOutTradeNo(request);
    }

}
