package com.github.automain.common.dao;

import com.github.automain.common.bean.TbDictionary;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TbDictionaryDao extends BaseDao<TbDictionary> {

    @SuppressWarnings("unchecked")
    public PageBean<TbDictionary> selectTableForCustomPage(ConnectionBean connection, TbDictionary bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbDictionary> pageParameterBean = new PageParameterBean<TbDictionary>();
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

    private String setSearchCondition(TbDictionary bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_dictionary WHERE is_delete = 0 ");
        if (bean.getSequenceNumber() != null) {
            sql.append(" AND sequence_number = ?");
            parameterList.add(bean.getSequenceNumber());
        }
        if (bean.getDictTableName() != null) {
            sql.append(" AND dict_table_name = ?");
            parameterList.add(bean.getDictTableName());
        }
        if (bean.getDictColumnName() != null) {
            sql.append(" AND dict_column_name = ?");
            parameterList.add(bean.getDictColumnName());
        }
        if (bean.getParentId() != null) {
            sql.append(" AND parent_id = ?");
            parameterList.add(bean.getParentId());
        }
        if (bean.getDictionaryName() != null) {
            sql.append(" AND dictionary_name = ?");
            parameterList.add(bean.getDictionaryName());
        }
        if (bean.getDictionaryValue() != null) {
            sql.append(" AND dictionary_value = ?");
            parameterList.add(bean.getDictionaryValue());
        }
        if (bean.getIsLeaf() != null) {
            sql.append(" AND is_leaf = ?");
            parameterList.add(bean.getIsLeaf());
        }
        return sql.toString();
    }

    public List<TbDictionary> selectValidTable(ConnectionBean connection, TbDictionary bean) throws SQLException {
        String sql = "SELECT * FROM tb_dictionary WHERE is_delete = '0' ORDER BY dict_table_name, dict_column_name, parent_id, sequence_number";
        return executeSelectReturnList(connection, sql, null, bean);
    }

    public List<String> selectTableNameList(ConnectionBean connection) throws Exception{
        ResultSet rs = null;
        List<String> tableNameList = new ArrayList<String>();
        try {
            String sql = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?";
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(PropertiesUtil.DATABASE_NAME));
            while (rs.next()) {
                tableNameList.add(rs.getString(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return tableNameList;
    }

    public List<String> selectColumnNameList(ConnectionBean connection, String tableName) throws Exception{
        ResultSet rs = null;
        List<String> columnList = new ArrayList<String>();
        try {
            String sql = "SELECT c.COLUMN_NAME FROM information_schema.COLUMNS c WHERE c.TABLE_SCHEMA = ? AND c.TABLE_NAME = ?";
            rs = executeSelectReturnResultSet(connection, sql, Arrays.asList(PropertiesUtil.DATABASE_NAME, tableName));
            while (rs.next()) {
                columnList.add(rs.getString(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return columnList;
    }

}