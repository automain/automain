package com.github.automain.user.dao;

import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbUserDao extends BaseDao<TbUser> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUser> selectTableForCustomPage(ConnectionBean connection, TbUser bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbUser bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_user WHERE is_delete = 0 ");
        if (bean.getCellphone() != null) {
            sql.append(" AND cellphone = ?");
            parameterList.add(bean.getCellphone());
        }
        if (bean.getUserName() != null) {
            sql.append(" AND user_name LIKE ?");
            parameterList.add(bean.getUserName() + "%");
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