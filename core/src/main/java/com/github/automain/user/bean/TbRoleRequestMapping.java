package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbRoleRequestMapping extends RequestUtil implements BaseBean<TbRoleRequestMapping> {

    // 角色路径关系ID
    private Long roleRequestMappingId;

    // 角色ID
    private Long roleId;

    // 请求路径
    private String requestUrl;

    // 是否删除(0:否,1;是)
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

    public Long getRoleRequestMappingId() {
        return roleRequestMappingId;
    }

    public void setRoleRequestMappingId(Long roleRequestMappingId) {
        this.roleRequestMappingId = roleRequestMappingId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_role_request_mapping";
    }

    @Override
    public String primaryKey() {
        return "role_request_mapping_id";
    }

    @Override
    public Long primaryValue() {
        return this.getRoleRequestMappingId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getRequestUrl() != null) {
            map.put("request_url", this.getRequestUrl());
        }
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        return map;
    }

    @Override
    public TbRoleRequestMapping beanFromResultSet(ResultSet rs) throws SQLException {
        TbRoleRequestMapping bean = new TbRoleRequestMapping();
        bean.setRoleRequestMappingId(rs.getLong("role_request_mapping_id"));
        bean.setRoleId(rs.getLong("role_id"));
        bean.setRequestUrl(rs.getString("request_url"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbRoleRequestMapping beanFromRequest(HttpServletRequest request) {
        TbRoleRequestMapping bean = new TbRoleRequestMapping();
        bean.setRoleRequestMappingId(getLong("roleRequestMappingId", request));
        bean.setRoleId(getLong("roleId", request));
        bean.setRequestUrl(getString("requestUrl", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}