package com.zhibai.ai.service;


import com.zhibai.ai.model.PageInfo;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.req.ImgHistoryPageReq;
import com.zhibai.ai.model.resp.HistoryInfoResp;
import com.zhibai.ai.model.resp.TaskReq;
import org.springframework.web.multipart.MultipartFile;

public interface DiscordService {

    Result<HistoryInfoResp> result(Long userId, Long historyId);

    Result<PageInfo<HistoryInfoResp>> queryAllHistory(ImgHistoryPageReq req);

    Result<Long> imagine(Long userId, String prompt);

    Result<Long> upscale(Long userId, Long historyId, Integer index);

    Result<Long> variation(Long userId, Long historyId, Integer index);

    Result<Long> reset(Long userId, Long historyId);

    Result<Long> describe(Long userId, MultipartFile file);

    Result<Void> notify(TaskReq taskReq);

}
