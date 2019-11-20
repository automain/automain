package com.github.automain.dao;

import com.github.automain.bean.SysFileRelation;
import com.github.automain.vo.SysFileRelationVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysFileRelationDao extends BaseDao {

    private static final SysFileRelation DEFAULT_BEAN = new SysFileRelation();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysFileRelation> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysFileRelation> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysFileRelation> selectTableForCustomPage(Connection connection, SysFileRelationVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysFileRelation> pageParamBean = new PageParamBean<SysFileRelation>()
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

    private static String setSearchCondition(SysFileRelationVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_file_relation WHERE is_valid = 1");
        if (StringUtils.isNotBlank(bean.getFileGid())) {
            sql.append(" AND file_gid = ?");
            paramList.add(bean.getFileGid());
        }
        if (StringUtils.isNotBlank(bean.getRecordTableName())) {
            sql.append(" AND record_table_name = ?");
            paramList.add(bean.getRecordTableName());
        }
        if (bean.getRecordId() != null) {
            sql.append(" AND record_id = ?");
            paramList.add(bean.getRecordId());
        }
        if (StringUtils.isNotBlank(bean.getRecordGid())) {
            sql.append(" AND record_gid = ?");
            paramList.add(bean.getRecordGid());
        }
        if (bean.getSequenceNumber() != null) {
            sql.append(" AND sequence_number = ?");
            paramList.add(bean.getSequenceNumber());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}