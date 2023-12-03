package com.zhibai.ai.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author xunbai
 * @Date 2023-05-14 17:03
 * @description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryInfoResp {

    private Long id;

    private String q;

    private String a;

    private String status;

    private String action;

    private String createTime;

}
