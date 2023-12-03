package com.zhibai.ai.controller;

import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.ImgHistoryPageReq;
import com.zhibai.ai.model.req.SubmitReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.TaskReq;
import com.zhibai.ai.security.UserThreadLocal;
import com.zhibai.ai.service.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class DiscordController {

    @Resource
    private DiscordService discordService;

    @GetMapping("/result")
    public Result<HistoryInfoResp> result(@RequestParam("historyId") Long historyId) {
        return discordService.result(UserThreadLocal.getUserId(), historyId);
    }

    @PostMapping("/queryAllHistory")
    public Result<PageInfo<HistoryInfoResp>> queryAllHistory(@RequestBody ImgHistoryPageReq req) {
        req.setUserId(UserThreadLocal.getUserId());
        return discordService.queryAllHistory(req);
    }

    @PostMapping("/imagine")
    public Result<Long> imagine(@RequestBody SubmitReq submitReq) {
        return discordService.imagine(UserThreadLocal.getUserId(), submitReq.getPrompt());
    }

    @PostMapping("/upscale")
    public Result<Long> upscale(@RequestBody SubmitReq submitReq) {
        return discordService.upscale(UserThreadLocal.getUserId(), submitReq.getHistoryId(), submitReq.getIndex());
    }

    @PostMapping("/variation")
    public Result<Long> variation(@RequestBody SubmitReq submitReq) {
        return discordService.variation(UserThreadLocal.getUserId(), submitReq.getHistoryId(), submitReq.getIndex());
    }

    @PostMapping("/reset")
    public Result<Long> reset(@RequestBody SubmitReq submitReq) {
        return discordService.reset(UserThreadLocal.getUserId(), submitReq.getHistoryId());
    }

    @PostMapping("/describe")
    public Result<Long> describe(@RequestParam("file") MultipartFile file) {
        return discordService.describe(UserThreadLocal.getUserId(), file);
    }

    @PostMapping("/notify")
    public Result<Void> notify(@RequestBody TaskReq taskReq) {
        return discordService.notify(taskReq);
    }
}
