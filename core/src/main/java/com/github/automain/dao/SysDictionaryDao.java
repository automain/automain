package com.github.automain.dao;

import com.github.automain.bean.SysDictionary;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParamBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SysDictionaryDao extends BaseDao<SysDictionary> {

    @SuppressWarnings("unchecked")
    public PageBean<SysDictionary> selectTableForCustomPage(ConnectionBean connection, SysDictionaryVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysDictionary> pageParamBean = new PageParamBean<SysDictionary>()
                .setConnection(connection)
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private String setSearchCondition(SysDictionaryVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_dictionary WHERE is_valid = 1 ");
        if (bean.getTableName() != null) {
            sql.append(" AND table_name = ?");
            paramList.add(bean.getTableName());
        }
        if (bean.getColumnName() != null) {
            sql.append(" AND column_name = ?");
            paramList.add(bean.getColumnName());
        }
        if (bean.getDictionaryKey() != null) {
            sql.append(" AND dictionary_key = ?");
            paramList.add(bean.getDictionaryKey());
        }
        if (bean.getCreateTime() != null ) {
            sql.append(" AND create_time >= ?");
            paramList.add(bean.getCreateTime());
        }
        if (bean.getCreateTimeEnd() != null) {
            sql.append(" AND create_time < ?");
            paramList.add(bean.getCreateTimeEnd());
        }
        if (bean.getUpdateTime() != null ) {
            sql.append(" AND update_time >= ?");
            paramList.add(bean.getUpdateTime());
        }
        if (bean.getUpdateTimeEnd() != null) {
            sql.append(" AND update_time < ?");
            paramList.add(bean.getUpdateTimeEnd());
        }
        if (bean.getDictionaryValue() != null) {
            sql.append(" AND dictionary_value = ?");
            paramList.add(bean.getDictionaryValue());
        }
        return sql.toString();
    }

    public List<DictionaryVO> selectAllDictionaryVO(ConnectionBean connection) throws Exception{
        return executeSelectReturnList(connection, "SELECT sd.table_name, sd.column_name, sd.dictionary_key, sd.dictionary_value FROM sys_dictionary sd WHERE sd.is_valid = 1", null, new DictionaryVO());
    }

    public List<String> selectDictionaryColumn(ConnectionBean connection, String tableName) throws Exception{
        ResultSet rs = null;
        List<String> result = new ArrayList<String>();
        try {
            rs = executeSelectReturnResultSet(connection, "SELECT sd.column_name FROM sys_dictionary sd WHERE sd.table_name = ? GROUP BY sd.column_name", List.of(tableName));
            while (rs.next()) {
                result.add(rs.getString("column_name"));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return result;
    }
}