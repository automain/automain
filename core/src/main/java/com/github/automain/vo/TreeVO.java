package com.github.automain.vo;

import java.util.List;

public class TreeVO {

    private Integer id;

    private String label;

    private List<TreeVO> children;

    public Integer getId() {
        return id;
    }

    public TreeVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public TreeVO setLabel(String label) {
        this.label = label;
        return this;
    }

    public List<TreeVO> getChildren() {
        return children;
    }

    public TreeVO setChildren(List<TreeVO> children) {
        this.children = children;
        return this;
    }

    @Override
    public String toString() {
        return "TreeVO{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
