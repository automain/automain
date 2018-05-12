package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbRole extends RequestUtil implements BaseBean<TbRole> {

    // 角色ID
    private Long roleId;

    // 角色名称
    private String roleName;

    // 角色标识
    private String roleLabel;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========

    // 是否有该权限(0:否,1:是)
    private Integer hasRole;

    public Integer getHasRole() {
        return hasRole;
    }

    public void setHasRole(Integer hasRole) {
        this.hasRole = hasRole;
    }

    // ========== additional column end ==========

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_role";
    }

    @Override
    public String primaryKey() {
        return "role_id";
    }

    @Override
    public Long primaryValue() {
        return this.getRoleId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getRoleLabel() != null) {
            map.put("role_label", this.getRoleLabel());
        }
        if (all || this.getRoleName() != null) {
            map.put("role_name", this.getRoleName());
        }
        return map;
    }

    @Override
    public TbRole beanFromResultSet(ResultSet rs) throws SQLException {
        TbRole bean = new TbRole();
        bean.setRoleId(rs.getLong("role_id"));
        bean.setRoleName(rs.getString("role_name"));
        bean.setRoleLabel(rs.getString("role_label"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbRole beanFromRequest(HttpServletRequest request) {
        TbRole bean = new TbRole();
        bean.setRoleId(getLong("roleId", request));
        bean.setRoleName(getString("roleName", request));
        bean.setRoleLabel(getString("roleLabel", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}