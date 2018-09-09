package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.container.DictionaryContainer;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/schedule/forward")
public class ScheduleForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "add":
                jspPath = "common/schedule_add";
                break;
            case "update":
            case "detail":
                Long scheduleId = getLong("scheduleId", request, 0L);
                TbSchedule bean = TB_SCHEDULE_SERVICE.selectTableById(connection, scheduleId);
                request.setAttribute("bean", bean);
                jspPath = "common/schedule_" + forwardType;
                break;
            default:
                jspPath = "common/schedule_tab";
        }
        request.setAttribute("delayUnitVOList", DictionaryContainer.getDictionary(jedis, "tb_schedule", "delay_unit", 0L));
        return jspPath;
    }
}