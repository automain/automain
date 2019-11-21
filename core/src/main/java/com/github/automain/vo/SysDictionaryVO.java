package com.github.automain.vo;

import com.github.automain.bean.SysDictionary;

public class SysDictionaryVO extends SysDictionary {

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

    public SysDictionaryVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysDictionaryVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysDictionaryVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysDictionaryVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    @Override
    public String toString() {
        return "SysDictionaryVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}