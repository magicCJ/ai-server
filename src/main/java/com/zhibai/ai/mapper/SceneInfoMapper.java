package com.zhibai.ai.mapper;

import com.zhibai.ai.model.domain.SceneInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneInfoMapper {


    int insertSelective(SceneInfoDO record);


    int updateByPrimaryKeySelective(SceneInfoDO record);

    SceneInfoDO queryById(@Param("id") Long id);

    List<SceneInfoDO> queryAll();

}