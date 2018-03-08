package com.github.automain.user.dao;

import com.github.automain.user.bean.TbRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbRequestMappingDao extends BaseDao<TbRequestMapping> {

    @SuppressWarnings("unchecked")
    public PageBean<TbRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRequestMapping bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbRequestMapping bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_request_mapping WHERE 1 = 1 ");
        if (bean.getRequestUrl() != null) {
            sql.append(" AND request_url = ?");
            parameterList.add(bean.getRequestUrl());
        }
        if (bean.getOperationClass() != null) {
            sql.append(" AND operation_class = ?");
            parameterList.add(bean.getOperationClass());
        }
        if (bean.getUrlComment() != null) {
            sql.append(" AND url_comment = ?");
            parameterList.add(bean.getUrlComment());
        }
        return sql.toString();
    }
}