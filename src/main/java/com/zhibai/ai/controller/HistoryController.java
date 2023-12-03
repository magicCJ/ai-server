package com.zhibai.ai.controller;

import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.HistoryPageReq;
import com.zhibai.ai.model.req.HistoryUpdateReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.HistoryTitleResp;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-21 16:26
 * @description
 **/

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @GetMapping("/queryHistoryTitle")
    public Result<List<HistoryTitleResp>> queryHistoryTitle() {
        return Result.success(historyService.queryHistoryTitle(UserThreadLocal.getUserId()));
    }

    @PostMapping("/historyDetail")
    public Result<PageInfo<HistoryInfoResp>> pageQueryHistory(@RequestBody HistoryPageReq req) {
        req.setUserId(UserThreadLocal.getUserId());
        return Result.success(historyService.queryHistoryDetail(req));
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody HistoryUpdateReq req) {
        req.setUserId(UserThreadLocal.getUserId());
        historyService.update(req);
        return Result.success();
    }

    @PostMapping("/del")
    public Result<Void> del(@RequestBody HistoryUpdateReq req) {
        req.setUserId(UserThreadLocal.getUserId());
        historyService.del(req);
        return Result.success();
    }

}
