package com.github.automain.common.dao;

import com.github.automain.common.bean.TbConfig;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbConfigDao extends BaseDao<TbConfig> {

    @SuppressWarnings("unchecked")
    public PageBean<TbConfig> selectTableForCustomPage(ConnectionBean connection, TbConfig bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbConfig> pageParameterBean = new PageParameterBean<TbConfig>();
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

    private String setSearchCondition(TbConfig bean, List<Object> parameterList, boolean isCountSql) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_config WHERE is_delete = 0 ");
        if (bean.getConfigKey() != null) {
            sql.append(" AND config_key = ?");
            parameterList.add(bean.getConfigKey());
        }
        if (bean.getConfigValue() != null) {
            sql.append(" AND config_value = ?");
            parameterList.add(bean.getConfigValue());
        }
        if (bean.getConfigComment() != null) {
            sql.append(" AND config_comment = ?");
            parameterList.add(bean.getConfigComment());
        }
        if (bean.getCreateTimeRange() != null) {
            sql.append(" AND create_time >= ? AND create_time <= ?");
            setTimeRange(bean.getCreateTimeRange(), parameterList);
        }
        if (bean.getUpdateTimeRange() != null) {
            sql.append(" AND update_time >= ? AND update_time <= ?");
            setTimeRange(bean.getUpdateTimeRange(), parameterList);
        }
        return sql.toString();
    }
}