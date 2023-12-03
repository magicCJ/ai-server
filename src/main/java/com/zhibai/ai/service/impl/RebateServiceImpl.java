package com.zhibai.ai.service.impl;

import com.zhibai.ai.manager.RebateRecordManager;
import com.zhibai.ai.manager.UserInfoManager;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.UserInfoDO;
import com.zhibai.ai.model.resp.RebateHomeResp;
import com.zhibai.ai.service.RebateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:13
 * @description
 **/
@Service
public class RebateServiceImpl implements RebateService {

    @Resource
    private UserInfoManager userInfoManager;

    @Resource
    private RebateRecordManager rebateRecordManager;

    @Override
    public Result<RebateHomeResp> getInfo(Long userId) {
        UserInfoDO userInfoDO = userInfoManager.queryById(userId);
        int invitationPeopleNumber = userInfoManager.countByInviterId(userId);
        BigDecimal totalRebateAmount = rebateRecordManager.totalRebateAmount(userId);
        int invitationOrderNumber = rebateRecordManager.countByUserId(userId);

        RebateHomeResp rebateHomeResp = new RebateHomeResp("", totalRebateAmount, userInfoDO.getWithdrawalBalance(), invitationPeopleNumber,invitationOrderNumber);
        return Result.success(rebateHomeResp);
    }
}
