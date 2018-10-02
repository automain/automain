package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TbRoleMenuDao extends BaseDao<TbRoleMenu> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRoleMenu> selectTableForCustomPage(ConnectionBean connection, TbRoleMenu bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbRoleMenu> pageParameterBean = new PageParameterBean<TbRoleMenu>();
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

    private String setSearchCondition(TbRoleMenu bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_role_menu WHERE is_delete = 0 ");
        if (bean.getMenuId() != null) {
            sql.append(" AND menu_id = ?");
            parameterList.add(bean.getMenuId());
        }
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        return sql.toString();
    }

    public int clearRoleByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "UPDATE tb_role_menu SET is_delete = '1' WHERE role_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(roleId));
    }

    public List<Long> selectMenuIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT menu_id FROM tb_role_menu WHERE is_delete = 0 AND role_id = ?";
        ResultSet rs = null;
        List<Long> menuIdList = new ArrayList<Long>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(roleId));
            while (rs.next()) {
                menuIdList.add(rs.getLong(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return menuIdList;
    }

}