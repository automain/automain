package com.github.automain.monitor.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.bean.DbStatus;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbStatusListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbStatus bean = new DbStatus();
        bean = bean.beanFromRequest(request);
        PageBean<DbStatus> pageBean = DB_STATUS_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "db_status/db_status_list";
    }
}