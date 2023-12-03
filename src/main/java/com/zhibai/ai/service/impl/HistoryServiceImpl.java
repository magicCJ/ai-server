package com.zhibai.ai.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.manager.ConversationHistoryManager;
import com.zhibai.ai.manager.ConversationTitleManager;
import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.domain.ConversationHistoryDO;
import com.zhibai.ai.model.domain.ConversationTitleDO;
import com.zhibai.ai.model.query.HistoryPageQuery;
import com.zhibai.ai.model.req.HistoryPageReq;
import com.zhibai.ai.model.req.HistoryUpdateReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.HistoryTitleResp;
import com.zhibai.ai.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xunbai
 * @Date 2023-05-21 16:08
 * @description
 **/
@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private ConversationHistoryManager conversationHistoryManager;

    @Resource
    private ConversationTitleManager conversationTitleManager;

    @Override
    public List<HistoryTitleResp> queryHistoryTitle(Long userId) {
        List<ConversationTitleDO> historyTitleList = conversationTitleManager.queryByUserId(userId, ShopEnum.TEXT.getType());
        List<HistoryTitleResp> result = new ArrayList<>();
        historyTitleList.forEach(conversationTitleDO -> {
            HistoryTitleResp historyTitleResp = new HistoryTitleResp();
            historyTitleResp.setId(conversationTitleDO.getId());
            historyTitleResp.setHistoryTitle(conversationTitleDO.getHistoryTitle());
            historyTitleResp.setBatchNo(conversationTitleDO.getBatchNo());
            result.add(historyTitleResp);
        });
        return result;
    }

    @Override
    public PageInfo<HistoryInfoResp> queryHistoryDetail(HistoryPageReq req) {
        HistoryPageQuery query = new HistoryPageQuery();
        query.setUserId(req.getUserId());
        query.setShopType(ShopEnum.TEXT.getType());
        query.setBatchNo(req.getBatchNo());
        int total = conversationHistoryManager.queryHistoryCount(query);
        if (total == 0) {
            return new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total);
        }

        query.setPage(req.getCurrentPage(), req.getPageSize());
        List<ConversationHistoryDO> conversationHistoryList = conversationHistoryManager.queryHistoryDetail(query);
        List<String> historyInfoList = conversationHistoryList.stream().map(ConversationHistoryDO::getHistoryInfo).collect(Collectors.toList());
        List<HistoryInfoResp> data = new ArrayList<>();
        historyInfoList.forEach(historyInfo -> {
            HistoryInfoResp historyInfoResp = JSON.parseObject(historyInfo, HistoryInfoResp.class);
            data.add(historyInfoResp);
        });
        return new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total, data);
    }

    @Override
    public void update(HistoryUpdateReq req) {
        ConversationTitleDO conversationTitleDO = new ConversationTitleDO();
        conversationTitleDO.setHistoryTitle(req.getTitle());
        conversationTitleDO.setUserId(req.getUserId());
        conversationTitleDO.setId(req.getId());
        conversationTitleManager.update(conversationTitleDO);
    }

    @Override
    public void del(HistoryUpdateReq req) {
        ConversationTitleDO conversationTitleDO = conversationTitleManager.queryById(req.getId(), req.getUserId());
        if (conversationTitleDO == null){
            throw new AiServerException(AiServerExceptionEnum.DATE_NOT_EXISTS);
        }
        conversationTitleManager.del(req.getId(), req.getUserId());
        conversationHistoryManager.delByBatchNoAndUserId(req.getUserId(), conversationTitleDO.getBatchNo());
    }
}
