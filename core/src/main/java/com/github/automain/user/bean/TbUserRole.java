package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbUserRole extends RequestUtil implements BaseBean<TbUserRole> {

    // 用户角色ID
    private Long userRoleId;

    // 用户ID
    private Long userId;

    // 角色ID
    private Long roleId;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_user_role";
    }

    @Override
    public String primaryKey() {
        return "user_role_id";
    }

    @Override
    public Long primaryValue() {
        return this.getUserRoleId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        if (all || this.getUserId() != null) {
            map.put("user_id", this.getUserId());
        }
        return map;
    }

    @Override
    public TbUserRole beanFromResultSet(ResultSet rs) throws SQLException {
        TbUserRole bean = new TbUserRole();
        bean.setUserRoleId(rs.getLong("user_role_id"));
        bean.setUserId(rs.getLong("user_id"));
        bean.setRoleId(rs.getLong("role_id"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbUserRole beanFromRequest(HttpServletRequest request) {
        TbUserRole bean = new TbUserRole();
        bean.setUserRoleId(getLong("userRoleId", request));
        bean.setUserId(getLong("userId", request));
        bean.setRoleId(getLong("roleId", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}