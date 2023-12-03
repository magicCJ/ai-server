package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.UserInfoDO;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:47
 * @description
 **/
public interface UserInfoManager {

    UserInfoDO queryById(Long id);

    UserInfoDO queryByOpenId(String openId);

    UserInfoDO insert(UserInfoDO userInfoDO);

    void update(UserInfoDO user);

    int countByInviterId(Long inviterId);

}
