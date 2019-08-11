package com.github.automain.common;

import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceNotFoundExecutor extends BaseExecutor {

    @Override
    protected void execute(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setJsonResult(request, "404", "资源未找到");
    }
}
