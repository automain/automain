package com.github.automain.controller;

import com.github.automain.common.BaseController;
import com.github.automain.common.JsonResponse;
import com.github.automain.common.RequestUri;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestController extends BaseController {

    @RequestUri("/test")
    public JsonResponse test(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        return JsonResponse.getSuccessJson("成功");
    }
}
