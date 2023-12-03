package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.ConversationTitleDO;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-20 22:00
 * @description
 **/
public interface ConversationTitleManager {

    int insert(ConversationTitleDO record);

    int update(ConversationTitleDO record);

    int del(Long id, Long userId);

    List<ConversationTitleDO> queryByUserId(Long userId, Integer shopType);

    ConversationTitleDO queryById(Long id, Long userId);
}
