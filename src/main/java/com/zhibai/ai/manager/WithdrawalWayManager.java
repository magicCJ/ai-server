package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.WithdrawalWayDO;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 21:28
 * @description
 **/
public interface WithdrawalWayManager {

    List<WithdrawalWayDO> queryByUserId(Long userId);

    int insert(WithdrawalWayDO record);

    int update(WithdrawalWayDO record);

    int delete(Long id, Long userId);
}
