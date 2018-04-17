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

    private String setSearchCondition(TbConfig bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_config WHERE 1 = 1 ");
        if (bean.getConfigKey() != null) {
            sql.append(" AND config_key = ?");
            parameterList.add(bean.getConfigKey());
        }
        if (bean.getConfigValue() != null) {
            sql.append(" AND config_value = ?");
            parameterList.add(bean.getConfigValue());
        }
        if (bean.getIsDelete() != null) {
            sql.append(" AND is_delete = ?");
            parameterList.add(bean.getIsDelete());
        }
        return sql.toString();
    }
}