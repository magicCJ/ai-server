package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserInfoMapper {

    int insertSelective(UserInfoDO record);

    int updateByPrimaryKeySelective(UserInfoDO record);

    UserInfoDO queryById(@Param("id") Long id);

    UserInfoDO queryByOpenId(@Param("openId") String openId);

    int updateBalance(@Param("id") Long id, @Param("withdrawalBalance")BigDecimal withdrawalBalance);

    int countByInviterId(@Param("inviterId") Long inviterId);

}