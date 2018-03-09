package com.github.automain.user.dao;

import com.github.automain.user.bean.TbUserRole;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
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
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbUserRole bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_user_role WHERE 1 = 1 ");
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        if (bean.getUserId() != null) {
            sql.append(" AND user_id = ?");
            parameterList.add(bean.getUserId());
        }
        if (bean.getIsDelete() != null) {
            sql.append(" AND is_delete = ?");
            parameterList.add(bean.getIsDelete());
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
        String sql = "UPDATE tb_user_role SET is_delete = '1' WHERE user_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(userId));
    }

}