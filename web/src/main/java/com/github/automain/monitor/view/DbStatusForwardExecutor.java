package com.github.automain.monitor.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.bean.DbStatus;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbStatusForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "monitor/db_status_tab";
    }
}