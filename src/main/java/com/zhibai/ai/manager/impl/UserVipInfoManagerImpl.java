package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.mapper.UserVipInfoMapper;
import com.zhibai.ai.model.domain.UserVipInfoDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 23:36
 * @description
 **/
@Service
public class UserVipInfoManagerImpl implements UserVipInfoManager {

    @Resource
    private UserVipInfoMapper userVipInfoMapper;

    @Override
    public int inset(UserVipInfoDO userVipInfoDO) {
        int i = userVipInfoMapper.insertSelective(userVipInfoDO);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(UserVipInfoDO userVipInfoDO) {
        int i = userVipInfoMapper.updateByPrimaryKeySelective(userVipInfoDO);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }

    @Override
    public UserVipInfoDO queryByUserIdAndType(Long userId, Integer type) {
        AssertUtil.isNotNull(userId, "userId");
        AssertUtil.isNotNull(type, "type");
        return userVipInfoMapper.queryByUserIdAndType(userId, type);
    }

    @Override
    public List<UserVipInfoDO> queryByUserId(Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        return userVipInfoMapper.queryByUserId(userId);
    }
}
