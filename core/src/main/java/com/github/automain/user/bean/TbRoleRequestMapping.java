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

    // 请求路径ID
    private Long requestMappingId;

    // 是否删除(0:否,1;是)
    private Integer isDelete;

    // ========== additional column begin ==========


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

    public Long getRequestMappingId() {
        return requestMappingId;
    }

    public void setRequestMappingId(Long requestMappingId) {
        this.requestMappingId = requestMappingId;
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
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        if (this.getRequestMappingId() != null) {
            map.put("request_mapping_id", this.getRequestMappingId());
        }
        if (this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbRoleRequestMapping beanFromResultSet(ResultSet rs) throws SQLException {
        TbRoleRequestMapping bean = new TbRoleRequestMapping();
        bean.setRoleRequestMappingId(rs.getLong("role_request_mapping_id"));
        bean.setRoleId(rs.getLong("role_id"));
        bean.setRequestMappingId(rs.getLong("request_mapping_id"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbRoleRequestMapping beanFromRequest(HttpServletRequest request) {
        TbRoleRequestMapping bean = new TbRoleRequestMapping();
        bean.setRoleRequestMappingId(getLong("roleRequestMappingId", request));
        bean.setRoleId(getLong("roleId", request));
        bean.setRequestMappingId(getLong("requestMappingId", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}