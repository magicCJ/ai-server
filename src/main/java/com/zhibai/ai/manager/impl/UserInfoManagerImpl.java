package com.zhibai.ai.manager.impl;

import com.zhibai.ai.manager.UserInfoManager;
import com.zhibai.ai.mapper.UserInfoMapper;
import com.zhibai.ai.model.domain.UserInfoDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:49
 * @description
 **/
@Service
public class UserInfoManagerImpl implements UserInfoManager {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoDO queryById(Long id) {
        AssertUtil.isNotNull(id, "id");
        return userInfoMapper.queryById(id);
    }

    @Override
    public UserInfoDO queryByOpenId(String openId) {
        AssertUtil.isNotNull(openId, "openId");
        return userInfoMapper.queryByOpenId(openId);
    }

    @Override
    public UserInfoDO insert(UserInfoDO userInfoDO) {
        userInfoMapper.insertSelective(userInfoDO);
        return userInfoDO;
    }

    @Override
    public void update(UserInfoDO userInfoDO) {
        userInfoMapper.updateByPrimaryKeySelective(userInfoDO);
    }

    @Override
    public int countByInviterId(Long inviterId) {
        AssertUtil.isNotNull(inviterId, "inviterId");
        return userInfoMapper.countByInviterId(inviterId);
    }

}
