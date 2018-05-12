package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbRoleMenu extends RequestUtil implements BaseBean<TbRoleMenu> {

    // 角色菜单ID
    private Long roleMenuId;

    // 角色ID
    private Long roleId;

    // 菜单ID
    private Long menuId;

    // 是否删除(0:否,1;是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(Long roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_role_menu";
    }

    @Override
    public String primaryKey() {
        return "role_menu_id";
    }

    @Override
    public Long primaryValue() {
        return this.getRoleMenuId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getMenuId() != null) {
            map.put("menu_id", this.getMenuId());
        }
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        return map;
    }

    @Override
    public TbRoleMenu beanFromResultSet(ResultSet rs) throws SQLException {
        TbRoleMenu bean = new TbRoleMenu();
        bean.setRoleMenuId(rs.getLong("role_menu_id"));
        bean.setRoleId(rs.getLong("role_id"));
        bean.setMenuId(rs.getLong("menu_id"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbRoleMenu beanFromRequest(HttpServletRequest request) {
        TbRoleMenu bean = new TbRoleMenu();
        bean.setRoleMenuId(getLong("roleMenuId", request));
        bean.setRoleId(getLong("roleId", request));
        bean.setMenuId(getLong("menuId", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}