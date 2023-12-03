package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.RebateRecordDO;

import java.math.BigDecimal;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:02
 * @description
 **/
public interface RebateRecordManager {

    int countByUserId(Long userId);

    BigDecimal totalRebateAmount(Long userId);

    int insert(RebateRecordDO record);

    int update(RebateRecordDO record);
}
