package com.github.automain.vo;

import com.github.automain.bean.SysRole;

import java.util.List;

public class SysRoleVO extends SysRole {

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
    // 创建时间结束
    private Integer createTimeEnd;
    // 更新时间结束
    private Integer updateTimeEnd;

    public int getPage() {
        return page;
    }

    public SysRoleVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysRoleVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysRoleVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysRoleVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public SysRoleVO setIdList(List<Integer> idList) {
        this.idList = idList;
        return this;
    }

    public Integer getCreateTimeEnd() {
        return createTimeEnd;
    }

    public SysRoleVO setCreateTimeEnd(Integer createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public Integer getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public SysRoleVO setUpdateTimeEnd(Integer updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
        return this;
    }
}