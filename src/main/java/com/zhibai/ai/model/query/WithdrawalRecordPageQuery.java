package com.zhibai.ai.model.query;


/**
 * @Author xunbai
 * @Date 2023-05-14 16:48
 * @description
 **/
public class WithdrawalRecordPageQuery {

    private Long userId;

    private Integer startIndex;

    private Integer pageSize;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
