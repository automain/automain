package com.github.automain.dao;

import com.github.automain.bean.TbRolePrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TbRolePrivilegeDao extends BaseDao<TbRolePrivilege> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRolePrivilege> selectTableForCustomPage(ConnectionBean connection, TbRolePrivilege bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbRolePrivilege> pageParameterBean = new PageParameterBean<TbRolePrivilege>();
        pageParameterBean.setConnection(connection);
        pageParameterBean.setBean(bean);
        pageParameterBean.setCountSql(countSql);
        pageParameterBean.setCountParameterList(countParameterList);
        pageParameterBean.setSql(sql);
        pageParameterBean.setParameterList(parameterList);
        pageParameterBean.setPage(page);
        pageParameterBean.setLimit(limit);
        return selectTableForPage(pageParameterBean);
    }

    private String setSearchCondition(TbRolePrivilege bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_role_privilege WHERE is_delete = 0 ");
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        if (bean.getPrivilegeId() != null) {
            sql.append(" AND privilege_id = ?");
            parameterList.add(bean.getPrivilegeId());
        }
        return sql.toString();
    }

    public Set<String> selectPrivilegeLabelByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT tp.privilege_label FROM tb_role_privilege trp INNER JOIN tb_privilege tp ON trp.privilege_id = tp.privilege_id WHERE trp.is_delete = 0 AND tp.is_delete = 0 AND trp.role_id = ?";
        ResultSet rs = null;
        Set<String> resultSet = new HashSet<String>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(roleId));
            while (rs.next()) {
                resultSet.add(rs.getString(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return resultSet;
    }

    public List<Long> selectPrivilegeIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT trp.privilege_id FROM tb_role_privilege trp WHERE trp.role_id = ? AND trp.is_delete = 0";
        ResultSet rs = null;
        List<Long> resultList = new ArrayList<Long>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(roleId));
            while (rs.next()) {
                resultList.add(rs.getLong(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return resultList;
    }

    public int clearRoleByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "UPDATE tb_role_privilege SET is_delete = '1' WHERE role_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(roleId));
    }
}