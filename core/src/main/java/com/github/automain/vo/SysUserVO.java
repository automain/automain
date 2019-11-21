package com.github.automain.vo;

import com.github.automain.bean.SysUser;

import java.util.List;

public class SysUserVO extends SysUser {

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
    // 权限标识
    private String roleLabel;

    public int getPage() {
        return page;
    }

    public SysUserVO setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SysUserVO setSize(int size) {
        this.size = size;
        return this;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public SysUserVO setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SysUserVO setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public List<String> getGidList() {
        return gidList;
    }

    public SysUserVO setGidList(List<String> gidList) {
        this.gidList = gidList;
        return this;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public SysUserVO setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
        return this;
    }

    @Override
    public String toString() {
        return "SysUserVO{" +
                "page=" + page +
                ", size=" + size +
                ", sortLabel='" + sortLabel + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", gidList=" + gidList +
                ", roleLabel='" + roleLabel + '\'' +
                '}';
    }
}