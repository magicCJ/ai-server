package com.zhibai.ai.service.impl;

import com.zhibai.ai.common.Constants;
import com.zhibai.ai.enums.PackageTypeEnum;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.manager.UserInfoManager;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.model.domain.UserInfoDO;
import com.zhibai.ai.model.domain.UserVipInfoDO;
import com.zhibai.ai.model.dto.WeixinUserInfo;
import com.zhibai.ai.model.dto.WeixinValidateInfo;
import com.zhibai.ai.model.resp.QRLoginResp;
import com.zhibai.ai.security.JwtTokenProvider;
import com.zhibai.ai.service.UserService;
import com.zhibai.ai.util.RedisUtil;
import com.zhibai.ai.util.UuidUtils;
import com.zhibai.ai.util.WeixinUtil;
import com.zhibai.ai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.SortedMap;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/26 21:56
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    WeixinUtil weixinUtil;

    @Resource
    RedisUtil redisUtil;

    @Resource
    UserInfoManager userInfoManager;

    @Resource
    private UserVipInfoManager userVipInfoManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    private void checkUserChanged(UserInfoDO user, WeixinUserInfo weixinUser) {
        boolean changed = false;
        if (!user.getHeadImg().equals(weixinUser.getHeadimgurl())) {
            user.setHeadImg(weixinUser.getHeadimgurl());
            changed = true;
        }
        if (!user.getNickName().equals(weixinUser.getNickname())) {
            user.setNickName(weixinUser.getNickname());
            changed = true;
        }
        if (!user.getSex().equals(weixinUser.getSex())) {
            user.setSex(weixinUser.getSex());
            changed = true;
        }
        if (!user.getCountry().equals(weixinUser.getCountry())) {
            user.setCountry(weixinUser.getCountry());
            changed = true;
        }
        if (!user.getProvince().equals(weixinUser.getProvince())) {
            user.setProvince(weixinUser.getProvince());
            changed = true;
        }
        if (!user.getCity().equals(weixinUser.getCity())) {
            user.setCity(weixinUser.getCity());
            changed = true;
        }
        if (changed) {
            userInfoManager.update(user);
        }
    }

    @Override
    public QRLoginResp preLogin(Long inviter, HttpServletRequest request) {
        String key = UuidUtils.getUUID();
        String redirectUrl = String.format("http://%s/user/login/result?key=%s&inviter=%s", StringUtil.getDomainName(request), key, inviter);
        return new QRLoginResp(key, redirectUrl);
    }

    @Override
    public UserInfoDO getUser(WeixinValidateInfo validateInfo) {
        return userInfoManager.queryByOpenId(validateInfo.getOpenid());
    }

    @Override
    public String verify(SortedMap<String, String> params) {
        return weixinUtil.verifySign(params);
    }

    @Override
    public String polling(String key) {
        // 询问key对应的用户是否已登录
        if (!redisUtil.hasKey(key)) {
            return null;
        } else {
            // 任意用户登录后都会有一个token
            return redisUtil.getString(key);
        }
    }

    @Override
    public String login(String code, Long inviterId, String key) {
        WeixinValidateInfo validateInfo = weixinUtil.validateLoginInfo(code);
        if (validateInfo == null) {
            return null;
        }
        String accessToken = validateInfo.getAccess_token();
        String openid = validateInfo.getOpenid();
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        WeixinUserInfo weixinUser = weixinUtil.getUserInfo(accessToken, openid);
        if (weixinUser == null) {
            return null;
        }
        UserInfoDO user = userInfoManager.queryByOpenId(openid);
        if (user == null) {
            // 写入用户
            user = new UserInfoDO(weixinUser.getNickname(), weixinUser.getOpenid(), weixinUser.getUnionid(), weixinUser.getHeadimgurl(), weixinUser.getSex(), weixinUser.getCountry(), weixinUser.getProvince(), weixinUser.getCity());
            user.setInviterId(inviterId);
            user = userInfoManager.insert(user);

            // 赠送10个gpt3.5体验次数
            UserVipInfoDO userVipInfoDO = new UserVipInfoDO();
            userVipInfoDO.setUserId(user.getId());
            userVipInfoDO.setType(ShopEnum.TEXT.getType());
            userVipInfoDO.setPackageType(PackageTypeEnum.PICTURE.getType());
            userVipInfoDO.setTotalNumber(10);
            userVipInfoDO.setTextRemainNumber(10);
            userVipInfoDO.setExpiredTime(LocalDateTime.now().plusDays(30));
            userVipInfoManager.inset(userVipInfoDO);

            // 赠送3次AI绘画体验次数
            UserVipInfoDO userPictureVipInfo = new UserVipInfoDO();
            userPictureVipInfo.setUserId(user.getId());
            userPictureVipInfo.setType(ShopEnum.PICTURE.getType());
            userPictureVipInfo.setPackageType(PackageTypeEnum.PICTURE.getType());
            userPictureVipInfo.setTotalNumber(3);
            userPictureVipInfo.setSpeedRemainNumber(3);
            userPictureVipInfo.setExpiredTime(LocalDateTime.now().plusDays(30));
            userVipInfoManager.inset(userVipInfoDO);

            if (inviterId != null){
                UserVipInfoDO inviterVipInfo = userVipInfoManager.queryByUserIdAndType(inviterId, ShopEnum.TEXT.getType());
                if (inviterVipInfo != null){
                    // 非无限次数则赠送邀请者20次体验次数
                    if (!Constants.INFINITE.equals(inviterVipInfo.getTotalNumber())){
                        inviterVipInfo.setTotalNumber(inviterVipInfo.getTotalNumber() + 20);
                        inviterVipInfo.setTextRemainNumber(inviterVipInfo.getTextRemainNumber() + 20);
                        inviterVipInfo.setExpiredTime(LocalDateTime.now().plusDays(30));
                        userVipInfoManager.update(inviterVipInfo);
                    }
                    // 无限次数已过期则赠送邀请者20次体验次数
                    if (Constants.INFINITE.equals(inviterVipInfo.getTotalNumber()) && LocalDateTime.now().isAfter(inviterVipInfo.getExpiredTime())){
                        inviterVipInfo.setTotalNumber(20);
                        inviterVipInfo.setTextRemainNumber(20);
                        inviterVipInfo.setExpiredTime(LocalDateTime.now().plusDays(30));
                        userVipInfoManager.update(inviterVipInfo);
                    }
                }
            }
        } else {
            // 更新信息
            checkUserChanged(user, weixinUser);
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        redisUtil.set(key, token, 60 * 60);
        return token;
    }

}
