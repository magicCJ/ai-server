package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.WithdrawalWayDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 13599
* @description 针对表【withdrawal_way(提现方式)】的数据库操作Mapper
* @createDate 2023-05-27 21:09:14
* @Entity com.xunbai.ai.model.domain.WithdrawalWay
*/
public interface WithdrawalWayMapper {

    List<WithdrawalWayDO> queryByUserId(@Param("userId") Long userId);
    int deleteByPrimaryKey(@Param("id") Long id, @Param("userId") Long userId);

    int insert(WithdrawalWayDO record);

    int updateByPrimaryKeySelective(WithdrawalWayDO record);

}
