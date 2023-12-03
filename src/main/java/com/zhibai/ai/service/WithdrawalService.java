package com.zhibai.ai.service;

import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.WithdrawalRecordReq;
import com.zhibai.ai.model.req.WithdrawalWayReq;
import com.zhibai.ai.model.req.WithdrawalWayResp;
import com.zhibai.ai.model.resp.WithdrawalRecordResp;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:26
 * @description
 **/
public interface WithdrawalService {

    Result<Void> addWithdrawalWay(WithdrawalWayReq req);

    Result<Void> updateWithdrawalWay(WithdrawalWayReq req);

    Result<Void> delWithdrawalWay(Long withdrawalWayId, Long userId);

    Result<List<WithdrawalWayResp>> queryWithdrawalWay(Long userId);

    Result<PageInfo<WithdrawalRecordResp>> queryWithdrawalRecord(WithdrawalRecordReq req);
}
