package com.github.automain.schedule;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;

public class ScheduleTaskController extends BaseController {

    @RequestUri("/schedule/test")
    public JsonResponse scheduleTest() throws Exception {
        return JsonResponse.getSuccessJson();
    }
}
