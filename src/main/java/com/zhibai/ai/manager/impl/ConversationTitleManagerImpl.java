package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.ConversationTitleManager;
import com.zhibai.ai.mapper.ConversationTitleMapper;
import com.zhibai.ai.model.domain.ConversationTitleDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-20 22:02
 * @description
 **/
@Service
public class ConversationTitleManagerImpl implements ConversationTitleManager {

    @Resource
    private ConversationTitleMapper conversationTitleMapper;

    @Override
    public int insert(ConversationTitleDO record) {
        int i = conversationTitleMapper.insertSelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(ConversationTitleDO record) {
        int i = conversationTitleMapper.updateByPrimaryKeySelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }

    @Override
    public int del(Long id, Long userId) {
        int i = conversationTitleMapper.del(id, userId);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_DELETE_ERROR);
        }
        return i;
    }

    @Override
    public List<ConversationTitleDO> queryByUserId(Long userId, Integer shopType) {
        AssertUtil.isNotNull(userId, "userId");
        AssertUtil.isNotNull(shopType, "shopType");
        return conversationTitleMapper.queryByUserId(userId, shopType);
    }

    @Override
    public ConversationTitleDO queryById(Long id, Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        AssertUtil.isNotNull(id, "id");
        return conversationTitleMapper.queryById(id, userId);
    }
}
