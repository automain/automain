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
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "add":
                jspPath = "db_status/db_status_add";
                break;
            case "update":
            case "detail":
                Long statusId = getLong("statusId", request, 0L);
                DbStatus bean = DB_STATUS_SERVICE.selectTableById(connection, statusId);
                request.setAttribute("bean", bean);
                jspPath = "db_status/db_status_" + forwardType;
                break;
            default:
                jspPath = "db_status/db_status_tab";
        }
        return jspPath;
    }
}