package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.WithdrawalRecordDO;
import com.zhibai.ai.model.query.WithdrawalRecordPageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 13599
* @description 针对表【withdrawal_record(提现记录)】的数据库操作Mapper
* @createDate 2023-05-27 21:09:14
* @Entity com.xunbai.ai.model.domain.WithdrawalRecord
*/
public interface WithdrawalRecordMapper {

    int countByUserId(@Param("userId") Long userId);

    List<WithdrawalRecordDO> queryByUserId(WithdrawalRecordPageQuery query);

    int insert(WithdrawalRecordDO record);

    int updateByPrimaryKeySelective(WithdrawalRecordDO record);

}
