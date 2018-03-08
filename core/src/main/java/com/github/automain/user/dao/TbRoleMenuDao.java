package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
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
}