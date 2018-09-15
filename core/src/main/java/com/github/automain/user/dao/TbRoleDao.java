package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbRoleDao extends BaseDao<TbRole> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRole> selectTableForCustomPage(ConnectionBean connection, TbRole bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbRole> pageParameterBean = new PageParameterBean<TbRole>();
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

    private String setSearchCondition(TbRole bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_role WHERE is_delete = 0 ");
        if (bean.getRoleLabel() != null) {
            sql.append(" AND role_label = ?");
            parameterList.add(bean.getRoleLabel());
        }
        if (bean.getRoleName() != null) {
            sql.append(" AND role_name = ?");
            parameterList.add(bean.getRoleName());
        }
        return sql.toString();
    }
}