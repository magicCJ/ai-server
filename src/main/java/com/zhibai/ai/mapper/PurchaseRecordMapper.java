package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.PurchaseRecordDO;
import org.apache.ibatis.annotations.Param;

/**
* @Author xunbai
* @Date 2023-05-13 23:24
* @description
**/
public interface PurchaseRecordMapper {

    int insertSelective(PurchaseRecordDO record);

    PurchaseRecordDO queryById(@Param("id") Long id);

    void updateByPrimaryKeySelective(PurchaseRecordDO record);

}