package com.github.automain.vo;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RoleVO implements BaseBean<RoleVO> {

    private String roleName;

    private String roleLabel;

    public String getRoleName() {
        return roleName;
    }

    public RoleVO setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public RoleVO setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
        return this;
    }


    @Override
    public String tableName() {
        return null;
    }

    @Override
    public Map<String, Object> columnMap(boolean b) {
        return null;
    }

    @Override
    public RoleVO beanFromResultSet(ResultSet rs) throws SQLException {
        return new RoleVO().setRoleName(rs.getString("role_name")).setRoleLabel(rs.getString("role_label"));
    }

    @Override
    public String toString() {
        return "RoleVO{" +
                "roleName='" + roleName + '\'' +
                ", roleLabel='" + roleLabel + '\'' +
                '}';
    }
}
