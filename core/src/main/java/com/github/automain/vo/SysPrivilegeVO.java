package com.github.automain.vo;

import com.github.automain.bean.SysPrivilege;

public class SysPrivilegeVO extends SysPrivilege {

    // 页码
    private int page;
    // 页大小
    private int size;
    // 排序字段
    private String sortLabel;
    // 排序顺序
    private String sortOrder;
    // 创建时间结束
    private Integer createTimeEnd;
    // 更新时间结束
    private Integer updateTimeEnd;

    public int getPage() {
        return page;
    }

    public SysPrivilegeVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysPrivilegeVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysPrivilegeVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysPrivilegeVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Integer getCreateTimeEnd() {
        return createTimeEnd;
    }

    public SysPrivilegeVO setCreateTimeEnd(Integer createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public Integer getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public SysPrivilegeVO setUpdateTimeEnd(Integer updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
        return this;
    }

    @Override
    public String toString() {
        return "SysPrivilegeVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }
}