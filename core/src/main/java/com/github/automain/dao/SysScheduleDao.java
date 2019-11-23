package com.github.automain.dao;

import com.github.automain.bean.SysSchedule;
import com.github.automain.vo.SysScheduleVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.ConnectionPool;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysScheduleDao extends BaseDao {

    private static final SysSchedule DEFAULT_BEAN = new SysSchedule();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysSchedule> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysSchedule> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysSchedule> selectTableForCustomPage(Connection connection, SysScheduleVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysSchedule> pageParamBean = new PageParamBean<SysSchedule>()
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

    private static String setSearchCondition(SysScheduleVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_schedule WHERE 1 = 1");
        if (StringUtils.isNotBlank(bean.getScheduleName())) {
            sql.append(" AND schedule_name LIKE ?");
            paramList.add(bean.getScheduleName() + "%");
        }
        if (StringUtils.isNotBlank(bean.getScheduleUrl())) {
            sql.append(" AND schedule_url = ?");
            paramList.add(bean.getScheduleUrl());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }

    public static Map<String, SysSchedule> selectUriScheduleMap(Connection connection) throws SQLException {
        String sql = "SELECT ss.schedule_url, ss.period, ss.start_execute_time FROM sys_schedule ss WHERE ss.is_valid = 1";
        ResultSet rs = null;
        Map<String, SysSchedule> result = new HashMap<String, SysSchedule>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, null);
            while (rs.next()) {
                result.put(rs.getString("schedule_url"), new SysSchedule().setPeriod(rs.getInt("period")).setStartExecuteTime(rs.getInt("start_execute_time")));
            }
            return result;
        } finally {
            ConnectionPool.close(rs);
        }
    }

    public static boolean checkScheduleUrlUseable(Connection connection, String scheduleUrl, Integer id) throws SQLException {
        Integer existId = executeSelectReturnInteger(connection, "SELECT ss.id FROM sys_schedule ss WHERE ss.schedule_url = ? LIMIT 1", List.of(scheduleUrl));
        return existId == null || existId.equals(id);
    }

    public static int updateLastExecuteTime(Connection connection, String scheduleUrl, Integer lastExecuteTime) throws SQLException {
        return executeUpdate(connection, "UPDATE sys_schedule ss SET ss.last_execute_time = ? WHERE ss.schedule_url = ?", List.of(lastExecuteTime, scheduleUrl));
    }
}