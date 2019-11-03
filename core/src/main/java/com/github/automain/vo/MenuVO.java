package com.github.automain.vo;

import java.util.List;

public class MenuVO {

    // 菜单路径
    private String menuPath;
    // 菜单名称
    private String menuName;
    // 菜单图标
    private String menuIcon;
    // 菜单子集
    private List<MenuVO> children;

    public String getMenuPath() {
        return menuPath;
    }

    public MenuVO setMenuPath(String menuPath) {
        this.menuPath = menuPath;
        return this;
    }

    public String getMenuName() {
        return menuName;
    }

    public MenuVO setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public MenuVO setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
        return this;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    public MenuVO setChildren(List<MenuVO> children) {
        this.children = children;
        return this;
    }
}
