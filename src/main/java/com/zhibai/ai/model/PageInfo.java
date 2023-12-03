package com.zhibai.ai.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-14 16:28
 * @description
 **/
@Data
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = -593737372403119566L;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 每页的数量
     */
    private Integer pageSize;


    /**
     * 是否最后一页
     */
    private Boolean lastPage;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 结果集
     */
    protected List<T> data;

    public PageInfo(Integer currentPage, Integer pageSize, Integer total) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.lastPage = true;
        this.total = total;
    }

    public PageInfo(Integer currentPage, Integer pageSize, Integer total, List<T> data) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.lastPage = (currentPage * pageSize) >= total;
        this.total = total;
        this.data = data;
    }
}