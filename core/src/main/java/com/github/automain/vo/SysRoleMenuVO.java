package com.github.automain.vo;

import com.github.automain.bean.SysRoleMenu;

import java.util.List;

public class SysRoleMenuVO extends SysRoleMenu {

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

    public SysRoleMenuVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysRoleMenuVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysRoleMenuVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysRoleMenuVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public SysRoleMenuVO setIdList(List<Integer> idList) {
        this.idList = idList;
        return this;
    }

    public Integer getCreateTimeEnd() {
        return createTimeEnd;
    }

    public SysRoleMenuVO setCreateTimeEnd(Integer createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    public Integer getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public SysRoleMenuVO setUpdateTimeEnd(Integer updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
        return this;
    }
}