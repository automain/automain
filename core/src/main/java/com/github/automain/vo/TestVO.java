package com.github.automain.vo;

import com.github.automain.bean.Test;

import java.util.List;

public class TestVO extends Test {

    // 页码
    private int page;
    // 页大小
    private int size;
    // 排序字段
    private String sortLabel;
    // 排序顺序
    private String sortOrder;
    // 删除用GID集合
    private List<String> gidList;
    // 创建时间结束
    private Integer createTimeEnd;
    // 测试字典集合
    private List<Integer> testDictionaryList;

    public int getPage() {
        return page;
    }

    public TestVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public TestVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public TestVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public TestVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<String> getGidList() {
        return gidList;
    }

    public TestVO setGidList(List<String> gidList) {
        this.gidList = gidList;
        return this;
    }

    public Integer getCreateTimeEnd() {
        return createTimeEnd;
    }

    public TestVO setCreateTimeEnd(Integer createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public List<Integer> getTestDictionaryList() {
        return testDictionaryList;
    }

    public TestVO setTestDictionaryList(List<Integer> testDictionaryList) {
        this.testDictionaryList = testDictionaryList;
        return this;
    }

    @Override
    public String toString() {
        return "TestVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", gidList=" + gidList +
                ", createTimeEnd=" + createTimeEnd +
                ", testDictionaryList=" + testDictionaryList +
                '}';
    }
}
