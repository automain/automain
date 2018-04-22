package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TbRoleRequestMappingDao extends BaseDao<TbRoleRequestMapping> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRoleRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRoleRequestMapping bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbRoleRequestMapping bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_role_request_mapping WHERE is_delete = 0 ");
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        if (bean.getRequestMappingId() != null) {
            sql.append(" AND request_mapping_id = ?");
            parameterList.add(bean.getRequestMappingId());
        }
        return sql.toString();
    }

    public Set<String> selectRequestUrlByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT trm.request_url FROM tb_request_mapping trm LEFT JOIN tb_role_request_mapping trrm ON trrm.request_mapping_id = trm.request_mapping_id AND trrm.is_delete = 0";
        List<Object> paramList = new ArrayList<Object>(1);
        if (!roleId.equals(1L)) {
            sql += " WHERE trrm.role_id = ?";
            paramList.add(roleId);
        }
        ResultSet rs = null;
        Set<String> resultSet = new HashSet<String>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, paramList);
            while (rs.next()) {
                resultSet.add(rs.getString(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return resultSet;
    }
}