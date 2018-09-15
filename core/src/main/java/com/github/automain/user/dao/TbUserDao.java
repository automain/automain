package com.github.automain.user.dao;

import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbUserDao extends BaseDao<TbUser> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUser> selectTableForCustomPage(ConnectionBean connection, TbUser bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbUser> pageParameterBean = new PageParameterBean<TbUser>();
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

    private String setSearchCondition(TbUser bean, List<Object> parameterList, boolean isCountSql) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_user WHERE is_delete = 0 ");
        if (bean.getCellphone() != null) {
            sql.append(" AND cellphone = ?");
            parameterList.add(bean.getCellphone());
        }
        if (bean.getUserName() != null) {
            sql.append(" AND user_name = ?");
            parameterList.add(bean.getUserName());
        }
        if (bean.getPasswordMd5() != null) {
            sql.append(" AND password_md5 = ?");
            parameterList.add(bean.getPasswordMd5());
        }
        if (bean.getCreateTimeRange() != null) {
            sql.append(" AND create_time >= ? AND create_time <= ?");
            setTimeRange(bean.getCreateTimeRange(), parameterList);
        }
        if (bean.getEmail() != null) {
            sql.append(" AND email = ?");
            parameterList.add(bean.getEmail());
        }
        return sql.toString();
    }
}