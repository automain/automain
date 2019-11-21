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

    @Override
    public String toString() {
        return "SysPrivilegeVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}