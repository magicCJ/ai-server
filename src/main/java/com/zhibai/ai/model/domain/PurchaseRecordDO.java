package com.zhibai.ai.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author xunbai
* @Date 2023-05-13 23:24
* @description 购买记录
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRecordDO {
    private Long id;

    /**
    * 创建时间
    */
    private Date gmtCreate;

    /**
    * 修改时间
    */
    private Date gmtModified;

    /**
    * 版本
    */
    private Integer version;

    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 支付渠道 1-支付宝，2-微信
    */
    private Byte payType;

    /**
     * 商品id
     */
    private Long shopId;

    /**
    * 商品类型，1-文字会员，2-画图会员，3-GPT4.0会员
    */
    private Byte shopType;

    /**
    * 商品名称
    */
    private String shopTitle;

    /**
    * 有效天数
    */
    private Byte effectiveDays;

    /**
    * 价格，单位：元
    */
    private BigDecimal price;

    /**
    * 扩展信息
    */
    private String feature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Byte getShopType() {
        return shopType;
    }

    public void setShopType(Byte shopType) {
        this.shopType = shopType;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public Byte getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Byte effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", version=").append(version);
        sb.append(", userId=").append(userId);
        sb.append(", payType=").append(payType);
        sb.append(", shopType=").append(shopType);
        sb.append(", shopTitle=").append(shopTitle);
        sb.append(", effectiveDays=").append(effectiveDays);
        sb.append(", price=").append(price);
        sb.append(", feature=").append(feature);
        sb.append("]");
        return sb.toString();
    }
}