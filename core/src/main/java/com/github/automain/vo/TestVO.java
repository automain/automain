package com.github.automain.vo;

import com.github.automain.bean.Test;

import java.util.List;

public class TestVO extends Test {

    // 页码
    private int page;
    // 页大小
    private int size;
    // 删除用GID集合
    private List<String> gidList;
    // 创建时间结束
    private Integer createTimeEnd;
    // 更新时间结束
    private Integer updateTimeEnd;

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

    public Integer getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public TestVO setUpdateTimeEnd(Integer updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
        return this;
    }

}
