package com.github.automain.monitor.dao;

import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.monitor.vo.DbSqlVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbStatusDao extends BaseDao<DbStatus> {

    private static DbSqlVO DB_SQL_VO = new DbSqlVO();

    @SuppressWarnings("unchecked")
    public PageBean<DbStatus> selectTableForCustomPage(ConnectionBean connection, DbStatus bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(DbStatus bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM db_status WHERE 1 = 1 ");
        if (bean.getCreateTimeRange() != null) {
            sql.append(" AND create_time >= ? AND create_time <= ?");
            setTimeRange(bean.getCreateTimeRange(), parameterList);
        }
        if (bean.getComCommit() != null) {
            sql.append(" AND com_commit = ?");
            parameterList.add(bean.getComCommit());
        }
        if (bean.getComDelete() != null) {
            sql.append(" AND com_delete = ?");
            parameterList.add(bean.getComDelete());
        }
        if (bean.getComInsert() != null) {
            sql.append(" AND com_insert = ?");
            parameterList.add(bean.getComInsert());
        }
        if (bean.getComRollback() != null) {
            sql.append(" AND com_rollback = ?");
            parameterList.add(bean.getComRollback());
        }
        if (bean.getComSelect() != null) {
            sql.append(" AND com_select = ?");
            parameterList.add(bean.getComSelect());
        }
        if (bean.getComUpdate() != null) {
            sql.append(" AND com_update = ?");
            parameterList.add(bean.getComUpdate());
        }
        if (bean.getPagesData() != null) {
            sql.append(" AND pages_data = ?");
            parameterList.add(bean.getPagesData());
        }
        if (bean.getPagesFree() != null) {
            sql.append(" AND pages_free = ?");
            parameterList.add(bean.getPagesFree());
        }
        if (bean.getPagesMisc() != null) {
            sql.append(" AND pages_misc = ?");
            parameterList.add(bean.getPagesMisc());
        }
        if (bean.getPoolName() != null) {
            sql.append(" AND pool_name = ?");
            parameterList.add(bean.getPoolName());
        }
        if (bean.getThreadsFree() != null) {
            sql.append(" AND threads_free = ?");
            parameterList.add(bean.getThreadsFree());
        }
        if (bean.getThreadsRunning() != null) {
            sql.append(" AND threads_running = ?");
            parameterList.add(bean.getThreadsRunning());
        }
        return sql.toString();
    }

    public DbStatus selectNowStatus(ConnectionBean connection, String poolName) throws SQLException {
        ResultSet rs = null;
        DbStatus result = new DbStatus();
        result.setCreateTime(new Timestamp(System.currentTimeMillis()));
        result.setPoolName(poolName);
        Long threadsConnected = 0L;
        try {
            String sql = "SHOW GLOBAL STATUS";
            rs = executeSelectReturnResultSet(connection, sql, null);
            while (rs.next()) {
                switch (rs.getString("Variable_name")) {
                    case "Com_select":
                        result.setComSelect(rs.getLong("Value"));
                        break;
                    case "Com_insert":
                        result.setComInsert(rs.getLong("Value"));
                        break;
                    case "Com_delete":
                        result.setComDelete(rs.getLong("Value"));
                        break;
                    case "Com_update":
                        result.setComUpdate(rs.getLong("Value"));
                        break;
                    case "Com_commit":
                        result.setComCommit(rs.getLong("Value"));
                        break;
                    case "Com_rollback":
                        result.setComRollback(rs.getLong("Value"));
                        break;
                    case "Threads_connected":
                        threadsConnected = rs.getLong("Value");
                        break;
                    case "Threads_running":
                        result.setThreadsRunning(rs.getLong("Value"));
                        break;
                    case "Innodb_buffer_pool_pages_data":
                        result.setPagesData(rs.getLong("Value"));
                        break;
                    case "Innodb_buffer_pool_pages_free":
                        result.setPagesFree(rs.getLong("Value"));
                        break;
                    case "Innodb_buffer_pool_pages_misc":
                        result.setPagesMisc(rs.getLong("Value"));
                        break;
                }
            }
            result.setThreadsFree(threadsConnected - result.getThreadsRunning());
        } finally {
            ConnectionPool.close(rs);
        }
        return result;
    }

    public List<DbSqlVO> selectDbSql(ConnectionBean connection, Timestamp startTime, Timestamp endTime) throws SQLException {
        String sql = "SELECT ds.create_time, ds.pool_name, ds.com_select, ds.com_insert, ds.com_delete, ds.com_update FROM db_status ds WHERE ds.create_time >= ? AND ds.create_time < ?";
        return executeSelectReturnList(connection, sql, Arrays.asList(startTime, endTime), DB_SQL_VO);
    }

}