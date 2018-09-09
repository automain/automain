package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@RequestUrl("/schedule/update")
public class ScheduleUpdateExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbSchedule bean = new TbSchedule();
        bean = bean.beanFromRequest(request);
        bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        TB_SCHEDULE_SERVICE.updateTable(connection, bean, false);
        setJsonResult(request, CODE_SUCCESS, "编辑成功");
        return null;
    }
}