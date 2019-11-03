package com.github.automain.dao;

import com.github.automain.bean.SysDictionary;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.ConnectionPool;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysDictionaryDao extends BaseDao<SysDictionary> {

    private static final SysDictionary DEFAULT_BEAN = new SysDictionary();

    public int insertIntoTable(Connection connection, SysDictionary bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysDictionary bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysDictionary> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysDictionary bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysDictionary bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysDictionary paramBean, SysDictionary newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int softDeleteTableById(Connection connection, SysDictionary bean) throws SQLException {
        return super.softDeleteTableById(connection, bean);
    }

    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int deleteTableById(Connection connection, SysDictionary bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysDictionary bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysDictionary selectTableById(Connection connection, SysDictionary bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysDictionary> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysDictionary selectOneTableByBean(Connection connection, SysDictionary bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysDictionary> selectTableByBean(Connection connection, SysDictionary bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysDictionary> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysDictionary> selectTableForPage(Connection connection, SysDictionary bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysDictionary> selectTableForCustomPage(Connection connection, SysDictionaryVO bean) throws Exception {
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
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_dictionary WHERE is_valid = 1");
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
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }

    public List<DictionaryVO> selectAllDictionaryVO(Connection connection) throws Exception{
        return executeSelectReturnList(connection, "SELECT sd.table_name, sd.column_name, sd.dictionary_key, sd.dictionary_value FROM sys_dictionary sd WHERE sd.is_valid = 1", null, new DictionaryVO());
    }

    public List<String> selectDictionaryColumn(Connection connection, String tableName) throws Exception{
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