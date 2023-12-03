package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.RebateRecordManager;
import com.zhibai.ai.mapper.RebateRecordMapper;
import com.zhibai.ai.model.domain.RebateRecordDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:03
 * @description
 **/
@Service
public class RebateRecordManagerImpl implements RebateRecordManager {

    @Resource
    private RebateRecordMapper rebateRecordMapper;

    @Override
    public int countByUserId(Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        return rebateRecordMapper.countByUserId(userId);
    }

    @Override
    public BigDecimal totalRebateAmount(Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        return rebateRecordMapper.totalRebateAmount(userId);
    }

    @Override
    public int insert(RebateRecordDO record) {
        int i = rebateRecordMapper.insert(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(RebateRecordDO record) {
        int i = rebateRecordMapper.updateByPrimaryKeySelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }
}
