package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.UserVipInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVipInfoMapper {

    int insertSelective(UserVipInfoDO record);

    int updateByPrimaryKeySelective(UserVipInfoDO record);

    UserVipInfoDO queryByUserIdAndType(@Param("userId") Long userId,
                                       @Param("type") Integer type);

    List<UserVipInfoDO> queryByUserId(@Param("userId") Long userId);
}