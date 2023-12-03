package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.RebateRecordDO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author 13599
* @description 针对表【rebate_record(返利记录)】的数据库操作Mapper
* @createDate 2023-05-27 21:52:14
* @Entity com.xunbai.ai.model.domain.RebateRecord
*/
public interface RebateRecordMapper {

    int countByUserId(@Param("userId") Long userId);

    BigDecimal totalRebateAmount(@Param("userId") Long userId);

    int insert(RebateRecordDO record);

    int updateByPrimaryKeySelective(RebateRecordDO record);

}
