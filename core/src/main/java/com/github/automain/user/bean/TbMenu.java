package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbMenu extends RequestUtil implements BaseBean<TbMenu>, Comparable {

    // 菜单ID
    private Long menuId;

    // 请求路径
    private String requestUrl;

    // 菜单名称
    private String menuName;

    // 菜单图标
    private String menuIcon;

    // 父级ID
    private Long parentId;

    // 顶级ID
    private Long topId;

    // 菜单排序
    private Integer sequenceNumber;

    // 是否默认展开(0:否,1:是)
    private Integer isSpread;

    // 是否是叶子节点(0:否,1:是)
    private Integer isLeaf;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========

    // 父级名称
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    // ========== additional column end ==========

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getIsSpread() {
        return isSpread;
    }

    public void setIsSpread(Integer isSpread) {
        this.isSpread = isSpread;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_menu";
    }

    @Override
    public String primaryKey() {
        return "menu_id";
    }

    @Override
    public Long primaryValue() {
        return this.getMenuId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getIsLeaf() != null) {
            map.put("is_leaf", this.getIsLeaf());
        }
        if (all || this.getIsSpread() != null) {
            map.put("is_spread", this.getIsSpread());
        }
        if (all || this.getMenuIcon() != null) {
            map.put("menu_icon", this.getMenuIcon());
        }
        if (all || this.getMenuName() != null) {
            map.put("menu_name", this.getMenuName());
        }
        if (all || this.getParentId() != null) {
            map.put("parent_id", this.getParentId());
        }
        if (all || this.getRequestUrl() != null) {
            map.put("request_url", this.getRequestUrl());
        }
        if (all || this.getSequenceNumber() != null) {
            map.put("sequence_number", this.getSequenceNumber());
        }
        if (all || this.getTopId() != null) {
            map.put("top_id", this.getTopId());
        }
        return map;
    }

    @Override
    public TbMenu beanFromResultSet(ResultSet rs) throws SQLException {
        TbMenu bean = new TbMenu();
        bean.setMenuId(rs.getLong("menu_id"));
        bean.setRequestUrl(rs.getString("request_url"));
        bean.setMenuName(rs.getString("menu_name"));
        bean.setMenuIcon(rs.getString("menu_icon"));
        bean.setParentId(rs.getLong("parent_id"));
        bean.setTopId(rs.getLong("top_id"));
        bean.setSequenceNumber(rs.getInt("sequence_number"));
        bean.setIsSpread(rs.getInt("is_spread"));
        bean.setIsLeaf(rs.getInt("is_leaf"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbMenu beanFromRequest(HttpServletRequest request) {
        TbMenu bean = new TbMenu();
        bean.setMenuId(getLong("menuId", request));
        bean.setRequestUrl(getString("requestUrl", request));
        bean.setMenuName(getString("menuName", request));
        bean.setMenuIcon(getString("menuIcon", request));
        bean.setParentId(getLong("parentId", request));
        bean.setTopId(getLong("topId", request));
        bean.setSequenceNumber(getInt("sequenceNumber", request));
        bean.setIsSpread(getInt("isSpread", request));
        bean.setIsLeaf(getInt("isLeaf", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof TbMenu){
            TbMenu target = (TbMenu) o;
            if (this.getParentId().equals(target.getParentId())) {
                return this.getSequenceNumber().compareTo(target.getSequenceNumber()) >= 0 ? 1 : -1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}