package com.github.automain.common.controller;

import com.github.automain.common.bean.JsonResponse;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ResourceNotFoundExecutor extends BaseExecutor {

    @Override
    protected JsonResponse execute(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return JsonResponse.getJson(404, "资源未找到");
    }
}
