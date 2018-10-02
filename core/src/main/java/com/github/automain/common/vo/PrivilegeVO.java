package com.github.automain.common.vo;

import java.util.List;

public class PrivilegeVO {

    // 权限ID
    private Long id;
    // 权限名称
    private String name;
    // 是否选中
    private Boolean checked;
    // 子权限
    private List<PrivilegeVO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<PrivilegeVO> getChildren() {
        return children;
    }

    public void setChildren(List<PrivilegeVO> children) {
        this.children = children;
    }
}
