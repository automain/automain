package com.github.automain.dao;

import com.github.automain.bean.TbUserRole;
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

public class TbUserRoleDao extends BaseDao<TbUserRole> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUserRole> selectTableForCustomPage(ConnectionBean connection, TbUserRole bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbUserRole> pageParameterBean = new PageParameterBean<TbUserRole>();
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

    private String setSearchCondition(TbUserRole bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_user_role WHERE is_delete = 0 ");
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        if (bean.getUserId() != null) {
            sql.append(" AND user_id = ?");
            parameterList.add(bean.getUserId());
        }
        return sql.toString();
    }

    public Set<Long> selectUserIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT user_id FROM tb_user_role WHERE is_delete = '0' AND role_id = ?";
        ResultSet rs = null;
        Set<Long> resultSet = new HashSet<Long>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(roleId));
            while (rs.next()) {
                resultSet.add(rs.getLong(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return resultSet;
    }

    public int clearUserRoleByUserId(ConnectionBean connection, Long userId) throws SQLException {
        String sql = "UPDATE tb_user_role SET is_delete = 1 WHERE user_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(userId));
    }

}