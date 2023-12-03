package com.zhibai.ai.service.impl;

import com.zhibai.ai.manager.WithdrawalRecordManager;
import com.zhibai.ai.manager.WithdrawalWayManager;
import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.WithdrawalRecordDO;
import com.zhibai.ai.model.domain.WithdrawalWayDO;
import com.zhibai.ai.model.query.WithdrawalRecordPageQuery;
import com.zhibai.ai.model.req.WithdrawalRecordReq;
import com.zhibai.ai.model.req.WithdrawalWayReq;
import com.zhibai.ai.model.req.WithdrawalWayResp;
import com.zhibai.ai.model.resp.WithdrawalRecordResp;
import com.zhibai.ai.service.WithdrawalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:26
 * @description
 **/
@Slf4j
@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Resource
    private WithdrawalRecordManager withdrawalRecordManager;

    @Resource
    private WithdrawalWayManager withdrawalWayManager;

    @Override
    public Result<Void> addWithdrawalWay(WithdrawalWayReq req) {
        WithdrawalWayDO withdrawalWayDO = new WithdrawalWayDO();
        BeanUtils.copyProperties(req, withdrawalWayDO);
        withdrawalWayManager.insert(withdrawalWayDO);
        return Result.success();
    }

    @Override
    public Result<Void> updateWithdrawalWay(WithdrawalWayReq req) {
        WithdrawalWayDO withdrawalWayDO = new WithdrawalWayDO();
        BeanUtils.copyProperties(req, withdrawalWayDO);
        withdrawalWayManager.update(withdrawalWayDO);
        return Result.success();
    }

    @Override
    public Result<Void> delWithdrawalWay(Long withdrawalWayId, Long userId) {
        withdrawalWayManager.delete(withdrawalWayId, userId);
        return Result.success();
    }

    @Override
    public Result<List<WithdrawalWayResp>> queryWithdrawalWay(Long userId) {
        List<WithdrawalWayDO> withdrawalWayList = withdrawalWayManager.queryByUserId(userId);
        List<WithdrawalWayResp> data = new ArrayList<>();
        withdrawalWayList.forEach(withdrawalWayDO -> {
            WithdrawalWayResp withdrawalWayResp = new WithdrawalWayResp();
            BeanUtils.copyProperties(withdrawalWayDO, withdrawalWayResp);
            data.add(withdrawalWayResp);
        });
        return Result.success(data);
    }

    @Override
    public Result<PageInfo<WithdrawalRecordResp>> queryWithdrawalRecord(WithdrawalRecordReq req) {
        int total = withdrawalRecordManager.countByUserId(req.getUserId());
        if (total == 0){
            return Result.success(new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total));
        }
        WithdrawalRecordPageQuery query = new WithdrawalRecordPageQuery();
        query.setUserId(req.getUserId());
        query.setPage(req.getCurrentPage(), req.getPageSize());
        List<WithdrawalRecordDO> withdrawalRecordList = withdrawalRecordManager.queryByUserId(query);
        List<WithdrawalRecordResp> data = new ArrayList<>();
        withdrawalRecordList.forEach(withdrawalRecordDO -> {
            WithdrawalRecordResp recordResp = new WithdrawalRecordResp();
            BeanUtils.copyProperties(withdrawalRecordDO, recordResp);
            data.add(recordResp);
        });
        return Result.success(new PageInfo<>(req.getCurrentPage(), req.getPageSize(), total, data));
    }
}
