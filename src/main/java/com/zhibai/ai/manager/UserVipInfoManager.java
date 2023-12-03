package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.UserVipInfoDO;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 23:35
 * @description
 **/
public interface UserVipInfoManager {

    int inset(UserVipInfoDO userVipInfoDO);

    int update(UserVipInfoDO userVipInfoDO);

    UserVipInfoDO queryByUserIdAndType(Long userId, Integer type);

    List<UserVipInfoDO> queryByUserId(Long userId);
}
