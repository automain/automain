package com.github.automain.schedule;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbStatusSchedule extends BaseExecutor {

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
                DbStatus status = DB_STATUS_SERVICE.selectNowStatus(connectionBean, poolName);
                DB_STATUS_SERVICE.insertIntoTable(connectionBean, status);
            } finally {
                ConnectionPool.closeConnectionBean(connectionBean);
            }
        }
        return null;
    }
}
