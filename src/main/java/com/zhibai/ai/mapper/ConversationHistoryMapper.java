package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.ConversationHistoryDO;
import com.zhibai.ai.model.query.HistoryPageQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationHistoryMapper {

    int insertSelective(ConversationHistoryDO record);

    int updateByPrimaryKeySelective(ConversationHistoryDO record);

    int delByBatchNoAndUserId( @Param("userId") Long userId, @Param("batchNo") String batchNo);

    int queryHistoryCount(HistoryPageQuery query);

    List<ConversationHistoryDO> queryHistoryDetail(HistoryPageQuery query);

    ConversationHistoryDO queryById(@Param("id") Long id, @Param("userId") Long userId);

    ConversationHistoryDO queryNewByBatchNoAndShopType(@Param("batchNo") String batchNo, @Param("shopType") Integer shopType);
}