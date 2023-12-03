package com.zhibai.ai.model.req;

import lombok.Data;

/**
 * @Author xunbai
 * @Date 2023-05-14 17:22
 * @description
 **/
@Data
public class HistoryPageReq extends BaseReq{

    private String batchNo;

    private Integer currentPage;

    private Integer pageSize;
}
