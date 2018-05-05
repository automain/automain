package com.github.automain.schedule;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.bean.DbSlowLog;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class SlowLogSchedule extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return HTTPUtil.checkInnerIp(request);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ConnectionBean connectionBean = null;
        for (String poolName : PropertiesUtil.POOL_NAMES) {
            try {
                connectionBean = ConnectionPool.getConnectionBean(poolName);
                List<DbSlowLog> slowLogList = DB_SLOW_LOG_SERVICE.selectNowSlowSql(connectionBean, poolName);
                inserSlowLog(connectionBean, slowLogList);
            } finally {
                ConnectionPool.closeConnectionBean(connectionBean);
            }
        }
        return null;
    }

    private void inserSlowLog(ConnectionBean connection, List<DbSlowLog> slowLogList) throws SQLException {
        for (DbSlowLog log : slowLogList) {
            DB_SLOW_LOG_SERVICE.insertIntoTable(connection, log);
        }
    }
}
