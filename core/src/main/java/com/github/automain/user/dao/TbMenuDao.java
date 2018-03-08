package com.github.automain.user.dao;

import com.github.automain.user.bean.TbMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TbMenuDao extends BaseDao<TbMenu> {

    @SuppressWarnings("unchecked")
    public PageBean<TbMenu> selectTableForCustomPage(ConnectionBean connection, TbMenu bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbMenu bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_menu WHERE 1 = 1 ");
        if (bean.getMenuName() != null) {
            sql.append(" AND menu_name = ?");
            parameterList.add(bean.getMenuName());
        }
        if (bean.getParentId() != null) {
            sql.append(" AND parent_id = ?");
            parameterList.add(bean.getParentId());
        }
        if (bean.getRequestUrl() != null) {
            sql.append(" AND request_url = ?");
            parameterList.add(bean.getRequestUrl());
        }
        if (bean.getMenuIcon() != null) {
            sql.append(" AND menu_icon = ?");
            parameterList.add(bean.getMenuIcon());
        }
        if (bean.getTopId() != null) {
            sql.append(" AND top_id = ?");
            parameterList.add(bean.getTopId());
        }
        if (bean.getSequenceNumber() != null) {
            sql.append(" AND sequence_number = ?");
            parameterList.add(bean.getSequenceNumber());
        }
        if (bean.getIsLeaf() != null) {
            sql.append(" AND is_leaf = ?");
            parameterList.add(bean.getIsLeaf());
        }
        if (bean.getIsDelete() != null) {
            sql.append(" AND is_delete = ?");
            parameterList.add(bean.getIsDelete());
        }
        return sql.toString();
    }

    public List<TbMenu> selectTbMenuByRoleId(ConnectionBean connection, TbMenu bean, Long roleId) throws SQLException {
        String sql = "SELECT tm.* FROM tb_menu tm LEFT JOIN tb_role_menu trm ON trm.menu_id = tm.menu_id AND trm.is_delete = '0' AND tm.is_delete = '0'";
        List<Object> paramList = new ArrayList<Object>(1);
        if (!roleId.equals(1L)) {
            sql += " WHERE trm.role_id = ?";
            paramList.add(roleId);
        }
        return executeSelectReturnList(connection, sql, paramList, bean);
    }
}