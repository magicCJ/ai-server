package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.ShopInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopInfoMapper {


    int insertSelective(ShopInfoDO record);


    int updateByPrimaryKeySelective(ShopInfoDO record);

    List<ShopInfoDO> queryAll();

    ShopInfoDO queryById(@Param("id") Long id);

}