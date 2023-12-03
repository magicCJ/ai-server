package com.zhibai.ai.manager.impl;

import com.zhibai.ai.manager.PurchaseRecordManager;
import com.zhibai.ai.mapper.PurchaseRecordMapper;
import com.zhibai.ai.model.domain.PurchaseRecordDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:19
 * @description
 **/
@Service
public class PurchaseRecordManagerImpl implements PurchaseRecordManager {

    @Resource
    private PurchaseRecordMapper purchaseRecordMapper;

    @Override
    public int insert(PurchaseRecordDO record) {
        return purchaseRecordMapper.insertSelective(record);
    }

    @Override
    public PurchaseRecordDO getRecordById(Long id) {
        return purchaseRecordMapper.queryById(id);
    }

    @Override
    public void updateRecord(PurchaseRecordDO record) {
        purchaseRecordMapper.updateByPrimaryKeySelective(record);
    }
}
