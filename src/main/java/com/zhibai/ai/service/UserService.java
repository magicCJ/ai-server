package com.zhibai.ai.service;

import com.zhibai.ai.model.domain.UserInfoDO;
import com.zhibai.ai.model.dto.WeixinValidateInfo;
import com.zhibai.ai.model.resp.QRLoginResp;

import javax.servlet.http.HttpServletRequest;
import java.util.SortedMap;

/**
 * @author: wangkecheng
 * @desc:
 * @version: 1.0
 * @time: 2023/05/26 21:55
 */
public interface UserService {

    String login(String code, Long inviterId, String key);

    QRLoginResp preLogin(Long inviterId, HttpServletRequest request);

    UserInfoDO getUser(WeixinValidateInfo validateInfo);

    String verify(SortedMap<String, String> params);

    String polling(String key);

}
