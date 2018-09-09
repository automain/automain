package com.github.automain.schedule;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.http.HTTPUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/schedule/dbstatus")
public class DbStatusScheduleExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.checkAuthority(jedis, request, response) || HTTPUtil.checkAPIToken(request, "appkey", null);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("schedule start, scheduleUrl = /schedule/dbstatus");
        ConnectionBean connectionBean = null;
        for (String poolName : PropertiesUtil.POOL_NAMES) {
            try {
                connectionBean = ConnectionPool.getConnectionBean(poolName);
                DbStatus status = DB_STATUS_SERVICE.selectNowStatus(connectionBean, poolName);
                DB_STATUS_SERVICE.insertIntoTable(connectionBean, status);
                TB_SCHEDULE_SERVICE.refreseLastExecuteTime(connection, "/schedule/dbstatus");
            } finally {
                ConnectionPool.closeConnectionBean(connectionBean);
            }
        }
        LOGGER.info("schedule end, scheduleUrl = /schedule/dbstatus");
        setJsonResult(request, CODE_SUCCESS, "执行成功");
        return null;
    }
}
