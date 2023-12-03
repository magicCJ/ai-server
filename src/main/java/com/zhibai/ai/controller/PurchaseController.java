package com.zhibai.ai.controller;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.OrderReq;
import com.zhibai.ai.model.req.PayReq;
import com.zhibai.ai.model.req.WxPayNoticeReq;
import com.zhibai.ai.model.resp.NoticeResp;
import com.zhibai.ai.model.resp.OrderResultResp;
import com.zhibai.ai.model.resp.PayOrderResp;
import com.zhibai.ai.service.PurchaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: kanson
 * @desc: 支付相关
 * @version: 1.0
 * @time: 2023/05/27 23:17
 */
@RestController
@RequestMapping("/pay")
public class PurchaseController {

    @Resource
    private PurchaseService purchaseService;

    /**
     * PC端点击微信支付
     */
    @PostMapping("/wxpay/order/native")
    public Result<PayOrderResp> queryAll(@RequestBody PayReq req, HttpServletRequest servletRequest) {
        return purchaseService.requestWxPay(req, servletRequest);
    }

    // PC端点击支付宝支付

    /**
     * 轮询查看订单支付结果
     */
    @PostMapping("/order/result")
    public Result<OrderResultResp> getOrderResult(@RequestBody OrderReq req) {
        OrderResultResp resp = purchaseService.getOrderPurchaseResult(req);
        if (resp == null) {
            return Result.failed("200", "操作成功");
        }
        return Result.success(resp);
    }

    // 微信支付通知
    @PostMapping("/wxpay/notice")
    public NoticeResp payNotice(@RequestBody WxPayNoticeReq req) {
        return purchaseService.payNotice(req);
    }

    // 支付宝支付通知



}
