package com.github.automain.controller;

import com.github.automain.common.RequestUri;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestController {

    @RequestUri("/test")
    public void test(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("test request");
    }
}
