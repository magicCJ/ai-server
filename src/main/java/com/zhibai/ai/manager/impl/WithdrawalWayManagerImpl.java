package com.zhibai.ai.manager.impl;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.manager.WithdrawalWayManager;
import com.zhibai.ai.mapper.WithdrawalWayMapper;
import com.zhibai.ai.model.domain.WithdrawalWayDO;
import com.zhibai.ai.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 21:28
 * @description
 **/
@Service
public class WithdrawalWayManagerImpl implements WithdrawalWayManager {

    @Resource
    private WithdrawalWayMapper withdrawalWayMapper;

    @Override
    public List<WithdrawalWayDO> queryByUserId(Long userId) {
        AssertUtil.isNotNull(userId, "userId");
        return withdrawalWayMapper.queryByUserId(userId);
    }

    @Override
    public int insert(WithdrawalWayDO record) {
        int i = withdrawalWayMapper.insert(record);;
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_SAVE_ERROR);
        }
        return i;
    }

    @Override
    public int update(WithdrawalWayDO record) {
        int i = withdrawalWayMapper.updateByPrimaryKeySelective(record);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_UPDATE_ERROR);
        }
        return i;
    }

    @Override
    public int delete(Long id, Long userId) {
        int i = withdrawalWayMapper.deleteByPrimaryKey(id, userId);
        if (i != 1){
            throw new AiServerException(AiServerExceptionEnum.DB_DATA_DELETE_ERROR);
        }
        return i;
    }
}
