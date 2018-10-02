package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbRolePrivilege extends RequestUtil implements BaseBean<TbRolePrivilege> {

    // 角色权限ID
    private Long rolePrivilegeId;

    // 角色ID
    private Long roleId;

    // 权限ID
    private Long privilegeId;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getRolePrivilegeId() {
        return rolePrivilegeId;
    }

    public void setRolePrivilegeId(Long rolePrivilegeId) {
        this.rolePrivilegeId = rolePrivilegeId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_role_privilege";
    }

    @Override
    public String primaryKey() {
        return "role_privilege_id";
    }

    @Override
    public Long primaryValue() {
        return this.getRolePrivilegeId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        if (all || this.getPrivilegeId() != null) {
            map.put("privilege_id", this.getPrivilegeId());
        }
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbRolePrivilege beanFromResultSet(ResultSet rs) throws SQLException {
        TbRolePrivilege bean = new TbRolePrivilege();
        bean.setRolePrivilegeId(rs.getLong("role_privilege_id"));
        bean.setRoleId(rs.getLong("role_id"));
        bean.setPrivilegeId(rs.getLong("privilege_id"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbRolePrivilege beanFromRequest(HttpServletRequest request) {
        TbRolePrivilege bean = new TbRolePrivilege();
        bean.setRolePrivilegeId(getLong("rolePrivilegeId", request));
        bean.setRoleId(getLong("roleId", request));
        bean.setPrivilegeId(getLong("privilegeId", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}