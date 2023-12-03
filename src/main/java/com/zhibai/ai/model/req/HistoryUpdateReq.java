package com.zhibai.ai.model.req;

import lombok.Data;

/**
 * @Author xunbai
 * @Date 2023-05-21 16:07
 * @description
 **/
@Data
public class HistoryUpdateReq extends BaseReq{

    private Long id;

    private String title;

}
