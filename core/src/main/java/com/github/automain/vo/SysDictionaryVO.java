package com.github.automain.vo;

import com.github.automain.bean.SysDictionary;

import java.util.List;

public class SysDictionaryVO extends SysDictionary {

    // 页码
    private int page;
    // 页大小
    private int size;
    // 删除用ID集合
    private List<Integer> idList;
    // 创建时间结束
    private Integer createTimeEnd;
    // 更新时间结束
    private Integer updateTimeEnd;

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

    public List<Integer> getIdList() {
        return idList;
    }

    public SysDictionaryVO setIdList(List<Integer> idList) {
        this.idList = idList;
        return this;
    }

    public Integer getCreateTimeEnd() {
        return createTimeEnd;
    }

    public SysDictionaryVO setCreateTimeEnd(Integer createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public Integer getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public SysDictionaryVO setUpdateTimeEnd(Integer updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
        return this;
    }
}