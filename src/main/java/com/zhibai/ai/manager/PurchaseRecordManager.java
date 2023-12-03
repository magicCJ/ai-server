package com.zhibai.ai.manager;

import com.zhibai.ai.model.domain.PurchaseRecordDO;

/**
 * @Author xunbai
 * @Date 2023-05-16 23:18
 * @description
 **/
public interface PurchaseRecordManager {

    int insert(PurchaseRecordDO record);

    PurchaseRecordDO getRecordById(Long id);

    void updateRecord(PurchaseRecordDO record);

}
