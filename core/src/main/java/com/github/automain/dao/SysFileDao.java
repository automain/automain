package com.github.automain.dao;

import com.github.automain.bean.SysFile;
import com.github.automain.vo.SysFileVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysFileDao extends BaseDao {

    private static final SysFile DEFAULT_BEAN = new SysFile();

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return deleteTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public static List<SysFile> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysFile> selectTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return selectTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public static List<SysFile> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysFile> selectTableForCustomPage(Connection connection, SysFileVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysFile> pageParamBean = new PageParamBean<SysFile>()
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

    private static String setSearchCondition(SysFileVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_file WHERE 1 = 1");
        if (StringUtils.isNotBlank(bean.getFileMd5())) {
            sql.append(" AND file_md5 = ?");
            paramList.add(bean.getFileMd5());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}