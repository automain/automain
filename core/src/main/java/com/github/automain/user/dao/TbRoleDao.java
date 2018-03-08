package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbRoleDao extends BaseDao<TbRole> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRole> selectTableForCustomPage(ConnectionBean connection, TbRole bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbRole bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_role WHERE 1 = 1 ");
        if (bean.getRoleLabel() != null) {
            sql.append(" AND role_label = ?");
            parameterList.add(bean.getRoleLabel());
        }
        if (bean.getRoleName() != null) {
            sql.append(" AND role_name = ?");
            parameterList.add(bean.getRoleName());
        }
        if (bean.getIsDelete() != null) {
            sql.append(" AND is_delete = ?");
            parameterList.add(bean.getIsDelete());
        }
        return sql.toString();
    }
}