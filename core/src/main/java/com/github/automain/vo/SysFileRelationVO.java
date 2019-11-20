package com.github.automain.vo;

import com.github.automain.bean.SysFileRelation;

import java.util.List;

public class SysFileRelationVO extends SysFileRelation {

    // 页码
    private int page;
    // 页大小
    private int size;
    // 排序字段
    private String sortLabel;
    // 排序顺序
    private String sortOrder;
    // 删除用ID集合
    private List<Integer> idList;

    public int getPage() {
        return page;
    }

    public SysFileRelationVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysFileRelationVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysFileRelationVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysFileRelationVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public SysFileRelationVO setIdList(List<Integer> idList) {
        this.idList = idList;
        return this;
    }

    @Override
    public String toString() {
        return "SysFileRelationVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", idList=" + idList +
                '}';
    }
}