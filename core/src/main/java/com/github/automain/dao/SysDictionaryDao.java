package com.github.automain.dao;

import com.github.automain.bean.SysDictionary;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysDictionaryDao extends BaseDao {

    private static final SysDictionary DEFAULT_BEAN = new SysDictionary();

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysDictionary> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysDictionary> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysDictionary> selectTableForCustomPage(SysDictionaryVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysDictionary> pageParamBean = new PageParamBean<SysDictionary>()
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(SysDictionaryVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_dictionary WHERE 1 = 1");
        if (StringUtils.isNotBlank(bean.getTableName())) {
            sql.append(" AND table_name = ?");
            paramList.add(bean.getTableName());
        }
        if (StringUtils.isNotBlank(bean.getColumnName())) {
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

    public static List<DictionaryVO> selectAllDictionaryVO() throws Exception{
        return executeSelectReturnList("SELECT sd.table_name, sd.column_name, sd.dictionary_key, sd.dictionary_value FROM sys_dictionary sd", null, new DictionaryVO());
    }

    public static List<String> selectDictionaryColumn(String tableName) throws Exception{
        return executeSelectReturnStringList("SELECT sd.column_name FROM sys_dictionary sd WHERE sd.table_name = ? GROUP BY sd.column_name", List.of(tableName));
    }
}