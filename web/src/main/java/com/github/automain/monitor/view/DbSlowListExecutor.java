package com.github.automain.monitor.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.monitor.bean.DbSlowLog;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/monitor/dbslow/list")
public class DbSlowListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbSlowLog bean = new DbSlowLog();
        bean = bean.beanFromRequest(request);
        PageBean<DbSlowLog> pageBean = DB_SLOW_LOG_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "monitor/db_slow_list";
    }
}