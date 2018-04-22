package com.github.automain.common.dao;

import com.github.automain.common.bean.TbConfig;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbConfigDao extends BaseDao<TbConfig> {

    @SuppressWarnings("unchecked")
    public PageBean<TbConfig> selectTableForCustomPage(ConnectionBean connection, TbConfig bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbConfig bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_config WHERE is_delete = 0 ");
        if (bean.getConfigKey() != null) {
            sql.append(" AND config_key = ?");
            parameterList.add(bean.getConfigKey());
        }
        if (bean.getConfigValue() != null) {
            sql.append(" AND config_value = ?");
            parameterList.add(bean.getConfigValue());
        }
        if (bean.getConfigComment() != null) {
            sql.append(" AND config_comment LIKE ?");
            parameterList.add(bean.getConfigComment() + "%");
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