package com.zhibai.ai.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:35
 * @description
 **/
@Data
public class PersonalInfoResp {

    private Long id;

    private String headImg;

    private String nickName;

    private List<VipInfoResp> vipInfos;


}
