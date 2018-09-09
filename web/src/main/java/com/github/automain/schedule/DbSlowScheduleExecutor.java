package com.github.automain.schedule;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.monitor.bean.DbSlowLog;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/schedule/dbslow")
public class DbSlowScheduleExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("schedule start, scheduleUrl = /schedule/dbslow");
        ConnectionBean connectionBean = null;
        for (String poolName : PropertiesUtil.POOL_NAMES) {
            try {
                connectionBean = ConnectionPool.getConnectionBean(poolName);
                List<DbSlowLog> slowLogList = DB_SLOW_LOG_SERVICE.selectNowSlowSql(connectionBean, poolName);
                DB_SLOW_LOG_SERVICE.batchInsertIntoTable(connectionBean, slowLogList);
                TB_SCHEDULE_SERVICE.refreseLastExecuteTime(connection, "/schedule/dbslow");
            } finally {
                ConnectionPool.closeConnectionBean(connectionBean);
            }
        }
        LOGGER.info("schedule end, scheduleUrl = /schedule/dbslow");
        setJsonResult(request, CODE_SUCCESS, "执行成功");
        return null;
    }
}
