package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.WithdrawalRecordManager;
import com.zhibai.ai.mapper.WithdrawalRecordMapper;
import com.zhibai.ai.model.domain.WithdrawalRecordDO;
import com.zhibai.ai.model.query.WithdrawalRecordPageQuery;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 21:25
 * @description
 **/
@Service
public class WithdrawalRecordManagerImpl implements WithdrawalRecordManager {

    @Resource
    private WithdrawalRecordMapper withdrawalRecordMapper;

    @Override
    public int countByUserId(Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        return withdrawalRecordMapper.countByUserId(userId);
    }

    @Override
    public List<WithdrawalRecordDO> queryByUserId(WithdrawalRecordPageQuery query) {
        AssertUtil.isNotNull(query.getUserId(), "userId");
        AssertUtil.isNotNull(query.getPageSize(), "pageSize");
        AssertUtil.isNotNull(query.getStartIndex(), "startIndex");
        return withdrawalRecordMapper.queryByUserId(query);
    }

    @Override
    public int insert(WithdrawalRecordDO record) {
        int i = withdrawalRecordMapper.insert(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(WithdrawalRecordDO record) {
        int i = withdrawalRecordMapper.updateByPrimaryKeySelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }
}
