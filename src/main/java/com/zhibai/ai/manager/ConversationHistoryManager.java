package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.ConversationHistoryDO;
import com.zhibai.ai.model.query.HistoryPageQuery;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-14 16:21
 * @description
 **/
public interface ConversationHistoryManager {

    int insert(ConversationHistoryDO record);

    int update(ConversationHistoryDO record);

    int delByBatchNoAndUserId(Long userId, String batchNo);

    int queryHistoryCount(HistoryPageQuery query);

    List<ConversationHistoryDO> queryHistoryDetail(HistoryPageQuery query);

    ConversationHistoryDO queryById(Long id, Long userId);

    ConversationHistoryDO queryNewByBatchNoAndShopType(String batchNo, Integer shopType);
}
