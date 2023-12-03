package com.zhibai.ai.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/27 23:24
 */
@Data
public class PayReq extends BaseReq {

    /**
     * 商品id
     */
    @NotNull
    private Long id;

    /**
     * 商品类型
     */
    @NotNull
    private Integer type;

}
