package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.WithdrawalRecordDO;
import com.zhibai.ai.model.query.WithdrawalRecordPageQuery;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 21:24
 * @description
 **/
public interface WithdrawalRecordManager {

    int countByUserId(Long userId);

    List<WithdrawalRecordDO> queryByUserId(WithdrawalRecordPageQuery query);

    int insert(WithdrawalRecordDO record);

    int update(WithdrawalRecordDO record);
}
