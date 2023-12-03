package com.zhibai.ai.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 0:53
 */
@Data
public class OrderReq {

    /**
     * 订单id
     */
    @NotNull
    private Long orderId;


}
