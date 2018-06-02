package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/home")
public class HomeExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/common/home";
    }
}