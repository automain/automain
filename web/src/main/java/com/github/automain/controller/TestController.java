package com.github.automain.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
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
