package com.github.automain.monitor.view;

import com.github.automain.common.BaseExecutor;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbStatusForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "sql":
                jspPath = "monitor/db_sql";
                break;
            case "transaction":
                jspPath = "monitor/db_transaction";
                break;
            case "thread":
                jspPath = "monitor/db_thread";
                break;
            case "memory":
                jspPath = "monitor/db_memory";
                break;
            default:
                jspPath = "monitor/db_sql";
        }
        return jspPath;
    }
}