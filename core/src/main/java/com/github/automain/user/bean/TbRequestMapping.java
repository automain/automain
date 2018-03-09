package com.github.automain.user.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbRequestMapping extends RequestUtil implements BaseBean<TbRequestMapping> {

    // 请求映射ID
    private Long requestMappingId;

    // 请求相对路径
    private String requestUrl;

    // 请求处理类的全路径
    private String operationClass;

    // 注释
    private String urlComment;

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

    public Long getRequestMappingId() {
        return requestMappingId;
    }

    public void setRequestMappingId(Long requestMappingId) {
        this.requestMappingId = requestMappingId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getOperationClass() {
        return operationClass;
    }

    public void setOperationClass(String operationClass) {
        this.operationClass = operationClass;
    }

    public String getUrlComment() {
        return urlComment;
    }

    public void setUrlComment(String urlComment) {
        this.urlComment = urlComment;
    }

    @Override
    public String tableName() {
        return "tb_request_mapping";
    }

    @Override
    public String primaryKey() {
        return "request_mapping_id";
    }

    @Override
    public Long primaryValue() {
        return this.getRequestMappingId();
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getRequestUrl() != null) {
            map.put("request_url", this.getRequestUrl());
        }
        if (this.getOperationClass() != null) {
            map.put("operation_class", this.getOperationClass());
        }
        if (this.getUrlComment() != null) {
            map.put("url_comment", this.getUrlComment());
        }
        return map;
    }

    @Override
    public TbRequestMapping beanFromResultSet(ResultSet rs) throws SQLException {
        TbRequestMapping bean = new TbRequestMapping();
        bean.setRequestMappingId(rs.getLong("request_mapping_id"));
        bean.setRequestUrl(rs.getString("request_url"));
        bean.setOperationClass(rs.getString("operation_class"));
        bean.setUrlComment(rs.getString("url_comment"));
        return bean;
    }

    @Override
    public TbRequestMapping beanFromRequest(HttpServletRequest request) {
        TbRequestMapping bean = new TbRequestMapping();
        bean.setRequestMappingId(getLong("requestMappingId", request));
        bean.setRequestUrl(getString("requestUrl", request));
        bean.setOperationClass(getString("operationClass", request));
        bean.setUrlComment(getString("urlComment", request));
        return bean;
    }
}