package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TbRoleMenuDao extends BaseDao<TbRoleMenu> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRoleMenu> selectTableForCustomPage(ConnectionBean connection, TbRoleMenu bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbRoleMenu bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_role_menu WHERE 1 = 1 ");
        if (bean.getMenuId() != null) {
            sql.append(" AND menu_id = ?");
            parameterList.add(bean.getMenuId());
        }
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            parameterList.add(bean.getRoleId());
        }
        if (bean.getIsDelete() != null) {
            sql.append(" AND is_delete = ?");
            parameterList.add(bean.getIsDelete());
        }
        return sql.toString();
    }

    public int clearRoleByMenuId(ConnectionBean connection, Long menuId) throws SQLException {
        String sql = "UPDATE tb_role_menu SET is_delete = '1' WHERE menu_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(menuId));
    }

    public int clearRoleByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "UPDATE tb_role_menu SET is_delete = '1' WHERE role_id = ?";
        return executeUpdate(connection, sql, Collections.singletonList(roleId));
    }

    public boolean checkExistChildByMenuId(ConnectionBean connection, Long menuId) throws SQLException {
        String sql = "SELECT tm.menu_id FROM tb_role_menu trm INNER JOIN tb_menu tm ON trm.menu_id = tm.menu_id WHERE tm.parent_id = ? AND trm.is_delete = '0'";
        ResultSet rs = null;
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(menuId));
            return rs.next();
        } finally {
            ConnectionPool.close(rs);
        }
    }

    public List<Long> selectMenuIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        String sql = "SELECT menu_id FROM tb_role_menu WHERE is_delete = '0' AND role_id = ?";
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