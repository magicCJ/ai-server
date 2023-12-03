package com.zhibai.ai.model.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 0:53
 */
@Data
public class WxPayNoticeReq {

    /**
     * 通知ID
     */
    @NotBlank
    @Length(min=1, max=36)
    private String id;

    /**
     * 通知创建时间
     */
    @NotBlank
    @Length(min=1, max=32)
    private String create_time;

    /**
     * 通知类型
     */
    @NotBlank
    @Length(min=1, max=32)
    private String event_type;

    /**
     * 通知数据类型
     */
    @NotBlank
    @Length(min=1, max=32)
    private String resource_type;

    /**
     * 通知数据
     */
    @NotNull
    private WxNoticeResource resource;

    /**
     * 回调摘要
     */
    @NotBlank
    @Length(min=1, max=64)
    private String summary;


}
