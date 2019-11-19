package com.github.automain.schedule;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ScheduleTaskController extends BaseController {

    @RequestUri("/schedule/test")
    public JsonResponse scheduleTest(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return JsonResponse.getSuccessJson();
    }
}
