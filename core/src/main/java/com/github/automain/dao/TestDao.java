package com.github.automain.dao;

import com.github.automain.bean.Test;
import com.github.automain.vo.TestVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestDao extends BaseDao {

    private static final Test DEFAULT_BEAN = new Test();

    public static int softDeleteTableByIdList(List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int softDeleteTableByGidList(List<String> gidList) throws SQLException {
        return softDeleteTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int deleteTableByGidList(List<String> gidList) throws SQLException {
        return deleteTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static List<Test> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<Test> selectTableByGidList(List<String> gidList) throws SQLException {
        return selectTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static List<Test> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<Test> selectTableForCustomPage(TestVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<Test> pageParamBean = new PageParamBean<Test>()
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(TestVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM test WHERE is_valid = 1");
        if (StringUtils.isNotBlank(bean.getGid())) {
            sql.append(" AND gid = ?");
            paramList.add(bean.getGid());
        }
        if (bean.getCreateTime() != null) {
            sql.append(" AND create_time >= ?");
            paramList.add(bean.getCreateTime());
        }
        if (bean.getCreateTimeEnd() != null) {
            sql.append(" AND create_time < ?");
            paramList.add(bean.getCreateTimeEnd());
        }
        if (CollectionUtils.isNotEmpty(bean.getTestDictionaryList())) {
            sql.append(" AND test_dictionary").append(makeInStr(bean.getTestDictionaryList()));
            paramList.addAll(bean.getTestDictionaryList());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}