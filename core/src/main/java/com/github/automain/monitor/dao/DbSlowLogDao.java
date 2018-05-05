package com.github.automain.monitor.dao;

import com.github.automain.monitor.bean.DbSlowLog;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbSlowLogDao extends BaseDao<DbSlowLog> {

    @SuppressWarnings("unchecked")
    public PageBean<DbSlowLog> selectTableForCustomPage(ConnectionBean connection, DbSlowLog bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(DbSlowLog bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM db_slow_log WHERE 1 = 1 ");
        if (bean.getCreateTimeRange() != null) {
            sql.append(" AND create_time >= ? AND create_time <= ?");
            setTimeRange(bean.getCreateTimeRange(), parameterList);
        }
        if (bean.getPoolName() != null) {
            sql.append(" AND pool_name = ?");
            parameterList.add(bean.getPoolName());
        }
        if (bean.getSlowDb() != null) {
            sql.append(" AND slow_db = ?");
            parameterList.add(bean.getSlowDb());
        }
        if (bean.getSlowSql() != null) {
            sql.append(" AND slow_sql = ?");
            parameterList.add(bean.getSlowSql());
        }
        if (bean.getSlowState() != null) {
            sql.append(" AND slow_state = ?");
            parameterList.add(bean.getSlowState());
        }
        if (bean.getSlowTime() != null) {
            sql.append(" AND slow_time = ?");
            parameterList.add(bean.getSlowTime());
        }
        return sql.toString();
    }

    public List<DbSlowLog> selectNowSlowSql(ConnectionBean connection, DbSlowLog bean, String poolName) throws SQLException {
        String sql = "SELECT NULL AS create_time, NULL AS pool_name, NULL AS slow_id, p.DB AS slow_db, p.TIME AS slow_time, p.STATE AS slow_state, p.INFO AS slow_sql FROM information_schema.processlist p WHERE p.COMMAND = 'Query' AND p.TIME > 2";
        List<DbSlowLog> slowLogList = executeSelectReturnList(connection, sql, null, bean);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (DbSlowLog log : slowLogList) {
            log.setCreateTime(now);
            log.setPoolName(poolName);
        }
        return slowLogList;
    }
}