package com.github.automain.common.vo;

import java.util.List;

public class MenuVO {

    // 菜单ID
    private Long id;
    // 菜单名称
    private String name;
    // 菜单跳转链接
    private String link;
    // 菜单图标
    private String icon;
    // 是否选中
    private Boolean checked;
    // 是否展开
    private Integer isSpread;
    // 菜单子集
    private List<MenuVO> children;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getIsSpread() {
        return isSpread;
    }

    public void setIsSpread(Integer isSpread) {
        this.isSpread = isSpread;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVO> children) {
        this.children = children;
    }
}
