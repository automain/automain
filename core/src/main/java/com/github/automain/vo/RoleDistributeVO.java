package com.github.automain.vo;

import java.util.List;

public class RoleDistributeVO {

    private Integer roleId;

    private List<Integer> distributeIdList;

    public Integer getRoleId() {
        return roleId;
    }

    public RoleDistributeVO setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public List<Integer> getDistributeIdList() {
        return distributeIdList;
    }

    public RoleDistributeVO setDistributeIdList(List<Integer> distributeIdList) {
        this.distributeIdList = distributeIdList;
        return this;
    }

    @Override
    public String toString() {
        return "RoleDistributeVO{" +
                "roleId=" + roleId +
                ", distributeIdList=" + distributeIdList +
                '}';
    }
}
