package com.zhibai.ai.service;

import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.req.HistoryPageReq;
import com.zhibai.ai.model.req.HistoryUpdateReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.HistoryTitleResp;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-21 16:05
 * @description
 **/
public interface HistoryService {

    List<HistoryTitleResp> queryHistoryTitle(Long userId);

    PageInfo<HistoryInfoResp> queryHistoryDetail(HistoryPageReq req);

    void update(HistoryUpdateReq req);

    void del(HistoryUpdateReq req);
}
