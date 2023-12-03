package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.ConversationTitleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 13599
* @description 针对表【conversation_title(会话标题历史信息)】的数据库操作Mapper
* @createDate 2023-05-20 21:52:19
* @Entity generator.domain.ConversationTitle
*/
public interface ConversationTitleMapper {

    int insertSelective(ConversationTitleDO record);

    int updateByPrimaryKeySelective(ConversationTitleDO record);

    int del(@Param("id") Long id, @Param("userId") Long userId);

    List<ConversationTitleDO> queryByUserId(@Param("userId") Long userId, @Param("shopType") Integer shopType);

    ConversationTitleDO queryById(@Param("id") Long id, @Param("userId") Long userId);

}
