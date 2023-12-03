package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.ConversationHistoryManager;
import com.zhibai.ai.mapper.ConversationHistoryMapper;
import com.zhibai.ai.model.domain.ConversationHistoryDO;
import com.zhibai.ai.model.query.HistoryPageQuery;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-14 16:53
 * @description
 **/
@Service
public class ConversationHistoryManagerImpl implements ConversationHistoryManager {

    @Resource
    private ConversationHistoryMapper conversationHistoryMapper;

    @Override
    public int insert(ConversationHistoryDO record) {
        int i = conversationHistoryMapper.insertSelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(ConversationHistoryDO record) {
        int i = conversationHistoryMapper.updateByPrimaryKeySelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }

    @Override
    public int delByBatchNoAndUserId(Long userId, String batchNo) {
        int i = conversationHistoryMapper.delByBatchNoAndUserId(userId, batchNo);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }

    @Override
    public int queryHistoryCount(HistoryPageQuery query) {
        AssertUtil.isNotNull(query.getUserId(), "userId");
        return conversationHistoryMapper.queryHistoryCount(query);
    }

    @Override
    public List<ConversationHistoryDO> queryHistoryDetail(HistoryPageQuery query) {
        AssertUtil.isNotNull(query.getUserId(), "userId");
        AssertUtil.isNotNull(query.getPageSize(), "pageSize");
        AssertUtil.isNotNull(query.getStartIndex(), "startIndex");
        return conversationHistoryMapper.queryHistoryDetail(query);
    }

    @Override
    public ConversationHistoryDO queryById(Long id, Long userId) {
        AssertUtil.isNotNull(id, "id");
        AssertUtil.isNotNull(userId, "userId");
        return conversationHistoryMapper.queryById(id, userId);
    }

    @Override
    public ConversationHistoryDO queryNewByBatchNoAndShopType(String batchNo, Integer shopType) {
        AssertUtil.isNotNull(batchNo, "batchNo");
        AssertUtil.isNotNull(shopType, "shopType");
        return conversationHistoryMapper.queryNewByBatchNoAndShopType(batchNo, shopType);
    }
}
