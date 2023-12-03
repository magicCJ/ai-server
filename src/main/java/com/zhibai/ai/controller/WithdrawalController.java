package com.zhibai.ai.controller;

import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.WithdrawalRecordReq;
import com.zhibai.ai.model.req.WithdrawalWayReq;
import com.zhibai.ai.model.req.WithdrawalWayResp;
import com.zhibai.ai.model.resp.WithdrawalRecordResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.WithdrawalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-27 22:44
 * @description
 **/
@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;

    @PostMapping("/addWay")
    public Result<Void> addWithdrawalWay(@RequestBody WithdrawalWayReq req){
        req.setUserId(UserThreadLocal.getUserId());
        return withdrawalService.addWithdrawalWay(req);
    }

    @PostMapping("/updateWay")
    public Result<Void> updateWithdrawalWay(@RequestBody WithdrawalWayReq req){
        req.setUserId(UserThreadLocal.getUserId());
        return withdrawalService.updateWithdrawalWay(req);
    }

    @PostMapping("/delWay")
    public Result<Void> delWithdrawalWay(@RequestBody WithdrawalWayReq req){
        return withdrawalService.delWithdrawalWay(req.getId(), UserThreadLocal.getUserId());
    }

    @GetMapping("/queryAllWay")
    public Result<List<WithdrawalWayResp>> queryWithdrawalWay(){
        return withdrawalService.queryWithdrawalWay(UserThreadLocal.getUserId());
    }

    @PostMapping("/queryWithdrawalRecord")
    public Result<PageInfo<WithdrawalRecordResp>> queryWithdrawalRecord(@RequestBody WithdrawalRecordReq req){
        req.setUserId(UserThreadLocal.getUserId());
        return withdrawalService.queryWithdrawalRecord(req);
    }
}
