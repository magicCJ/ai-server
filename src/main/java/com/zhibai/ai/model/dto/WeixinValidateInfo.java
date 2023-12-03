package com.zhibai.ai.model.dto;

import lombok.Data;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 03:25
 */
@Data
public class WeixinValidateInfo {

    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同，默认过期时间为7200s
     */
    private String access_token;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private Integer expires_in;

    /**
     * 用户刷新access_token，过期时间为30天
     */
    private String refresh_token;

    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * 是否为快照页模式虚拟账号，只有当用户是快照页模式虚拟账号时返回，值为1
     */
    private String is_snapshotuser;

    /**
     * 用户统一标识（针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的），只有当scope为"snsapi_userinfo"时返回
     */
    @Deprecated
    private String unionid;

}
