package com.zhibai.ai.model.query;


/**
 * @Author xunbai
 * @Date 2023-05-14 16:48
 * @description
 **/
public class HistoryPageQuery {

    private Long userId;

    private Integer shopType;

    private Long sceneId;

    private String batchNo;

    private Integer startIndex;

    private Integer pageSize;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPage(Integer currentPage, Integer pageSize) {
        this.startIndex = (currentPage - 1) * pageSize;
        this.pageSize = pageSize;
    }


}
