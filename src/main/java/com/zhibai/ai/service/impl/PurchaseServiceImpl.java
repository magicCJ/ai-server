package com.zhibai.ai.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.zhibai.ai.common.Constants;
import com.zhibai.ai.enums.OrderStatus;
import com.zhibai.ai.enums.PackageTypeEnum;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.manager.PurchaseRecordManager;
import com.zhibai.ai.manager.ShopInfoManager;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.PurchaseRecordDO;
import com.zhibai.ai.model.domain.ShopInfoDO;
import com.zhibai.ai.model.domain.UserVipInfoDO;
import com.zhibai.ai.model.dto.OrderFeatureDTO;
import com.zhibai.ai.model.req.OrderReq;
import com.zhibai.ai.model.req.PayReq;
import com.zhibai.ai.model.req.WxPayNoticeReq;
import com.zhibai.ai.model.resp.NoticeResp;
import com.zhibai.ai.model.resp.OrderResultResp;
import com.zhibai.ai.model.resp.PayOrderResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.PurchaseService;
import com.zhibai.ai.util.DateUtils;
import com.zhibai.ai.util.RedisUtil;
import com.zhibai.ai.util.WxPayUtil;
import com.zhibai.ai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static com.zhibai.ai.common.AiServerExceptionEnum.SECURITY_ERROR;
import static com.zhibai.ai.common.Constants.INFINITE;
import static com.zhibai.ai.common.Constants.ORDER_TITLE;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 23:20
 */
@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    @Resource
    private PurchaseRecordManager purchaseRecordManager;

    @Resource
    private ShopInfoManager shopInfoManager;

    @Resource
    private WxPayUtil wxPayUtil;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserVipInfoManager userVipInfoManager;

    /**
     * 创建订单
     * @param shopInfo
     * @param userId
     * @param payType
     * @return
     */
    private PurchaseRecordDO createPurchaseRecord(ShopInfoDO shopInfo, Long userId, Byte payType) {
        PurchaseRecordDO record = new PurchaseRecordDO();
        record.setUserId(userId);
        record.setPayType(payType);
        record.setShopId(shopInfo.getId());
        record.setShopTitle(shopInfo.getShopTitle());
        record.setShopType((byte) shopInfo.getType().intValue());
        record.setEffectiveDays((byte) shopInfo.getEffectiveDays().intValue());
        record.setPrice(shopInfo.getPrice());
        record.setVersion(OrderStatus.WAITING_PAY.getCode());

        purchaseRecordManager.insert(record);
        return record;
    }

    /**
     * 金额单位转换
     *
     * @param price 原始价格
     * @return
     */
    private int amountUnitConversion(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private String check(PurchaseRecordDO order, HttpServletRequest servletRequest) {
        // 拼接微信支付参数
        Long orderId = order.getId();

        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(amountUnitConversion(order.getPrice()));
        request.setAmount(amount);

        request.setDescription(String.format("%s-%s", ORDER_TITLE, order.getShopTitle()));
        request.setOutTradeNo(String.valueOf(orderId));

        String notifyUrl = String.format("https://%s/pay/wxpay/notice", StringUtil.getDomainName(servletRequest));
        request.setNotifyUrl(notifyUrl);
        return wxPayUtil.getPayUrl(request);
    }


    private UserVipInfoDO addVipOrShopNum(Long userId, ShopInfoDO shopInfoDO) {
        UserVipInfoDO userVipInfoDO = userVipInfoManager.queryByUserIdAndType(userId, shopInfoDO.getType());
        ShopEnum shopEnum = ShopEnum.getByType(shopInfoDO.getType());
        switch (Objects.requireNonNull(shopEnum)) {
            case TEXT:
                if (Constants.INFINITE.equals(userVipInfoDO.getTotalNumber())) {
                    LocalDateTime expiredTime = (userVipInfoDO.getExpiredTime().isAfter(LocalDateTime.now())) ? userVipInfoDO.getExpiredTime().plusDays(shopInfoDO.getEffectiveDays()) : LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays());
                    userVipInfoDO.setExpiredTime(expiredTime);
                } else {
                    userVipInfoDO.setExpiredTime(LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays()));
                }
                userVipInfoDO.setPackageType(PackageTypeEnum.MEMBERSHIP.getType());
                userVipInfoDO.setTotalNumber(Constants.INFINITE);
                userVipInfoDO.setTextRemainNumber(Constants.INFINITE);
                userVipInfoManager.update(userVipInfoDO);
                break;
            case PICTURE:
                if (Constants.INFINITE.equals(userVipInfoDO.getTotalNumber())) {
                    LocalDateTime expiredTime = (userVipInfoDO.getExpiredTime().isAfter(LocalDateTime.now())) ? userVipInfoDO.getExpiredTime().plusDays(shopInfoDO.getEffectiveDays()) : LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays());
                    userVipInfoDO.setExpiredTime(expiredTime);
                } else {
                    userVipInfoDO.setExpiredTime(LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays()));
                }
                userVipInfoDO.setPackageType(PackageTypeEnum.MEMBERSHIP.getType());
                userVipInfoDO.setTotalNumber(Constants.INFINITE);
                userVipInfoDO.setSpeedRemainNumber(shopInfoDO.getNumber());
                userVipInfoDO.setRelaxRemainNumber(Constants.INFINITE);
                userVipInfoManager.update(userVipInfoDO);
                break;
            case GPT4:
                if (userVipInfoDO == null) {
                    userVipInfoDO = new UserVipInfoDO();
                    userVipInfoDO.setUserId(userId);
                    userVipInfoDO.setType(ShopEnum.GPT4.getType());
                    userVipInfoDO.setPackageType(PackageTypeEnum.MEMBERSHIP.getType());
                    userVipInfoDO.setExpiredTime(LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays()));
                    userVipInfoDO.setTotalNumber(shopInfoDO.getNumber());
                    userVipInfoDO.setTextRemainNumber(shopInfoDO.getNumber());
                    userVipInfoManager.inset(userVipInfoDO);
                } else {
                    LocalDateTime expiredTime = (userVipInfoDO.getExpiredTime().isAfter(LocalDateTime.now())) ? userVipInfoDO.getExpiredTime().plusDays(shopInfoDO.getEffectiveDays()) : LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays());
                    userVipInfoDO.setExpiredTime(expiredTime);
                    userVipInfoDO.setTotalNumber(userVipInfoDO.getTotalNumber() + shopInfoDO.getNumber());
                    userVipInfoDO.setTextRemainNumber(userVipInfoDO.getTextRemainNumber() + shopInfoDO.getNumber());
                    userVipInfoManager.update(userVipInfoDO);
                }
                break;
            default:
                return userVipInfoDO;
        }
        return userVipInfoDO;
    }

    @Override
    public Result<PayOrderResp> requestWxPay(PayReq req, HttpServletRequest servletRequest) {
        // 查看商品信息
        ShopInfoDO shopInfo = shopInfoManager.queryById(req.getId());
        if (shopInfo == null) {
            return Result.failed("1901", String.format("商品%s不存在", req));
        }
        if (req.getUserId() == null && UserThreadLocal.getUserId() == null) {
            return Result.failed(SECURITY_ERROR);
        }
        if (req.getUserId() == null) {
            req.setUserId(UserThreadLocal.getUserId());
        }

        PurchaseRecordDO order;
        // 下单未支付商品缓存，15分钟内有效：拼接用户id-商品id
        String key = String.format("%s-%s", req.getUserId(), req.getId());
        if (redisUtil.hasKey(key)) {
            order = purchaseRecordManager.getRecordById(Long.valueOf(String.valueOf(redisUtil.get(key))));
        } else {
            // 创建订单
            order = createPurchaseRecord(shopInfo, req.getUserId(), (byte) 2);
            redisUtil.set(key, order.getId(), 15 * 60);
        }
        String codeUrl;
        try {
            codeUrl = check(order, servletRequest);
        } catch (Exception e) {
            order = createPurchaseRecord(shopInfo, req.getUserId(), (byte) 2);
            redisUtil.set(key, order.getId(), 15 * 60);
            codeUrl = check(order, servletRequest);
        }
        // 下发支付地址
        return Result.success(new PayOrderResp(order.getId(), codeUrl));
    }

    private void fillResult(OrderResultResp resp, UserVipInfoDO userVipInfoDO) {
        resp.setDay(DateUtils.days(userVipInfoDO.getExpiredTime(), LocalDateTime.now()));
        resp.setTotal(userVipInfoDO.getTotalNumber());
        if (Objects.equals(userVipInfoDO.getType(), ShopEnum.TEXT.getType()) || Objects.equals(userVipInfoDO.getType(), ShopEnum.GPT4.getType()) ) {
            resp.setAvailable(userVipInfoDO.getTextRemainNumber());
        } else if (Objects.equals(userVipInfoDO.getType(), ShopEnum.PICTURE.getType())) {
            int remainNumber = Objects.equals(userVipInfoDO.getTotalNumber(), INFINITE) ? userVipInfoDO.getRelaxRemainNumber() : userVipInfoDO.getSpeedRemainNumber();
            resp.setAvailable(remainNumber);
        }
    }

    @Override
    public OrderResultResp getOrderPurchaseResult(OrderReq req) {
        String key = String.valueOf(req.getOrderId());
        OrderResultResp resp = new OrderResultResp();
        resp.setLevel(1);
        // todo: 无限的话，得去统计使用记录
        resp.setUsed(0);
        if (!redisUtil.hasKey(key)) {
            // 查询商户订单
            Transaction transaction = wxPayUtil.getTransaction(key);
            if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
                // 发现已经支付了，则主动更新订单状态
                PurchaseRecordDO record = purchaseRecordManager.getRecordById(req.getOrderId());
                if (record == null) {
                    throw new RuntimeException(String.format(String.format("商户订单号不存在: %s", req.getOrderId())));
                }

                OrderFeatureDTO orderFeature = new OrderFeatureDTO(transaction.getTransactionId(),
                        transaction.getTradeType().name(), transaction.getTradeState().name(),
                        transaction.getBankType(), transaction.getSuccessTime(),
                        transaction.getPayer().getOpenid());
                // 拼接结果返回
                record.setVersion(OrderStatus.FINISHED_PAY.getCode());
                record.setFeature(JSON.toJSONString(orderFeature));
                purchaseRecordManager.updateRecord(record);
                if (!redisUtil.hasKey(key)) {
                    redisUtil.set(key, JSON.toJSONString(record), 15 * 60);
                }

                String vipKey = String.format("VIP:%s-%s", record.getUserId(), req.getOrderId());
                ShopInfoDO shopInfo = shopInfoManager.queryById(record.getShopId());
                UserVipInfoDO userVipInfo;
                if (!redisUtil.hasKey(vipKey)) {
                    redisUtil.set(vipKey, 1, 15 * 60);
                    // 如果还没通知，则这里更新次数
                    userVipInfo = addVipOrShopNum(record.getUserId(), shopInfo);
                } else {
                    userVipInfo = userVipInfoManager.queryByUserIdAndType(record.getUserId(), shopInfo.getType());
                }
                fillResult(resp, userVipInfo);
                resp.setOrderId(req.getOrderId());
                resp.setUserId(record.getUserId());
                resp.setProductType(Integer.valueOf(record.getShopType()));
                resp.setLabel(record.getShopTitle());
                resp.setCreateTime(DateUtils.format(record.getGmtCreate()));
                resp.setUpdateTime(DateUtils.format(record.getGmtModified()));
                return resp;
            } else {
                return null;
            }
        } else {
            // 支付成功，下发订单记录
            PurchaseRecordDO record = JSON.parseObject(String.valueOf(redisUtil.get(key)), PurchaseRecordDO.class);
            resp.setOrderId(req.getOrderId());
            resp.setUserId(record.getUserId());
            UserVipInfoDO userVipInfo = userVipInfoManager.queryByUserIdAndType(record.getUserId(), Integer.valueOf(record.getShopType()));
            if (userVipInfo == null) {
                userVipInfo = new UserVipInfoDO();
                userVipInfo.setUserId(record.getUserId());
                userVipInfo.setType(Integer.valueOf(record.getShopType()));
                userVipInfo.setPackageType(PackageTypeEnum.MEMBERSHIP.getType());
                ShopInfoDO shopInfoDO = shopInfoManager.queryById(record.getShopId());
                userVipInfo.setExpiredTime(LocalDateTime.now().plusDays(shopInfoDO.getEffectiveDays()));
                userVipInfo.setTotalNumber(shopInfoDO.getNumber());
                userVipInfo.setTextRemainNumber(shopInfoDO.getNumber());
                userVipInfoManager.inset(userVipInfo);
            }
            fillResult(resp, userVipInfo);
            resp.setProductType(Integer.valueOf(record.getShopType()));
            resp.setLabel(record.getShopTitle());
            resp.setCreateTime(DateUtils.format(record.getGmtCreate()));
            resp.setUpdateTime(DateUtils.format(record.getGmtModified()));
            return resp;
        }
    }

    @Override
    public NoticeResp payNotice(WxPayNoticeReq req) {
        log.info("WxPayNoticeReq: {}", req);
        NoticeResp resp = new NoticeResp();
        JSONObject noticeInfo = wxPayUtil.decryptNotice(req);
        // 商户订单号
        String outTradeNo = noticeInfo.getString("out_trade_no");
        // 交易状态
        String tradeState = noticeInfo.getString("trade_state");

        PurchaseRecordDO record = purchaseRecordManager.getRecordById(Long.valueOf(outTradeNo));
        if (record == null) {
            throw new RuntimeException(String.format(String.format("商户订单号不存在: %s", outTradeNo)));
        }

        OrderFeatureDTO orderFeature = new OrderFeatureDTO(noticeInfo.getString("transaction_id"),
                noticeInfo.getString("trade_type"), noticeInfo.getString("trade_state_desc"),
                noticeInfo.getString("bank_type"), noticeInfo.getString("success_time"),
                noticeInfo.getJSONObject("payer").getString("openid"));

        // 更新订单状态
        if ("SUCCESS".equals(tradeState)) {
            // 附加描述
            record.setVersion(OrderStatus.FINISHED_PAY.getCode());
            record.setFeature(JSON.toJSONString(orderFeature));
            purchaseRecordManager.updateRecord(record);
            if (!redisUtil.hasKey(outTradeNo)) {
                redisUtil.set(outTradeNo, JSON.toJSONString(record), 15 * 60);
            }

            // 支付成功
            String vipKey = String.format("VIP:%s-%s", record.getUserId(), outTradeNo);
            if (!redisUtil.hasKey(vipKey)) {
                // 添加标志，说明已经更新次数了
                redisUtil.set(vipKey, 1, 15 * 60);
                // 如果还没通知，则这里更新次数
                ShopInfoDO shopInfo = shopInfoManager.queryById(record.getShopId());
                addVipOrShopNum(record.getUserId(), shopInfo);
                // todo: 分成，有上级需要给上级分成
            }
            return null;
        } else if (Arrays.asList("CLOSED", "REFUND").contains(tradeState)) {
            // 取消支付
            record.setVersion(OrderStatus.CANCEL_PAY.getCode());
            record.setFeature(JSON.toJSONString(orderFeature));
            purchaseRecordManager.updateRecord(record);
            return null;
        } else if ("PAYERROR".equals(tradeState)) {
            // 支付失败
            return new NoticeResp("FAIL", "失败");
        }
        return null;
    }


}
