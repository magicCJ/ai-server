package com.zhibai.ai.service;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.OrderReq;
import com.zhibai.ai.model.req.PayReq;
import com.zhibai.ai.model.req.WxPayNoticeReq;
import com.zhibai.ai.model.resp.NoticeResp;
import com.zhibai.ai.model.resp.OrderResultResp;
import com.zhibai.ai.model.resp.PayOrderResp;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 23:18
 */
public interface PurchaseService {

    Result<PayOrderResp> requestWxPay(PayReq req, HttpServletRequest servletRequest);

    OrderResultResp getOrderPurchaseResult(OrderReq req);

    NoticeResp payNotice(WxPayNoticeReq req);

}
