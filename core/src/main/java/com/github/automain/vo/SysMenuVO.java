package com.github.automain.vo;

import com.github.automain.bean.SysMenu;

public class SysMenuVO extends SysMenu {

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

    public SysMenuVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysMenuVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysMenuVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysMenuVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    @Override
    public String toString() {
        return "SysMenuVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}