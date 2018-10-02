package com.github.automain.common.vo;

public class BreadcrumbVO {

    // 父级菜单ID
    private Long parentId;

    // 菜单名称
    private String menuName;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
