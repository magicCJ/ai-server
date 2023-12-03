package com.zhibai.ai.service.impl;

import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.common.Constants;
import com.zhibai.ai.enums.PackageTypeEnum;
import com.zhibai.ai.enums.ShopEnum;
import com.zhibai.ai.manager.UserInfoManager;
import com.zhibai.ai.manager.UserVipInfoManager;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.model.domain.UserInfoDO;
import com.zhibai.ai.model.domain.UserVipInfoDO;
import com.zhibai.ai.model.resp.PersonalInfoResp;
import com.zhibai.ai.model.resp.VipInfoResp;
import com.zhibai.ai.service.PersonalService;
import com.zhibai.ai.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-16 22:46
 * @description
 **/
@Slf4j
@Service
public class PersonalServiceImpl implements PersonalService {

    @Resource
    private UserInfoManager userInfoManager;

    @Resource
    private UserVipInfoManager userVipInfoManager;

    @Override
    public Result<PersonalInfoResp> getPersonalInfo(Long userId) {
        UserInfoDO userInfoDO = userInfoManager.queryById(userId);
        if (userInfoDO == null){
            return Result.failed(AiServerExceptionEnum.SECURITY_ERROR);
        }

        List<UserVipInfoDO> userVipInfoList = userVipInfoManager.queryByUserId(userId);
        List<VipInfoResp> vipInfos = new ArrayList<>();
        userVipInfoList.forEach(userVipInfo -> {
            VipInfoResp vipInfoResp = new VipInfoResp();
            vipInfoResp.setType(userVipInfo.getType());
            if (PackageTypeEnum.PICTURE.getType().equals(userVipInfo.getPackageType())){
                vipInfoResp.setVipTitle(PackageTypeEnum.getDescByType(userVipInfo.getPackageType()));
            } else {
                vipInfoResp.setVipTitle(ShopEnum.getDescByType(userVipInfo.getType()) + PackageTypeEnum.getDescByType(userVipInfo.getPackageType()));
            }
            vipInfoResp.setTotalNumber(convertNumber(userVipInfo.getTotalNumber()));
            vipInfoResp.setTextRemainNumber(convertNumber(userVipInfo.getTextRemainNumber()));
            vipInfoResp.setRelaxRemainNumber(convertNumber(userVipInfo.getRelaxRemainNumber()));
            vipInfoResp.setSpeedRemainNumber(convertNumber(userVipInfo.getSpeedRemainNumber()));
            vipInfoResp.setExpiredTime(DateUtils.format(userVipInfo.getExpiredTime()));
            vipInfos.add(vipInfoResp);
        });

        PersonalInfoResp personalInfoResp = new PersonalInfoResp();
        personalInfoResp.setId(userInfoDO.getId());
        personalInfoResp.setNickName(userInfoDO.getNickName());
        personalInfoResp.setHeadImg(userInfoDO.getHeadImg());
        personalInfoResp.setVipInfos(vipInfos);
        return Result.success(personalInfoResp);
    }


    private String convertNumber(Integer number){
        if (number == null){
            return null;
        }
        if (Constants.INFINITE.equals(number)){
            return "不限次";
        } else {
            return String.valueOf(number);
        }
    }
}
