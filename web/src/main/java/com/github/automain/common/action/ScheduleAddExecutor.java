package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@RequestUrl("/schedule/add")
public class ScheduleAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbSchedule bean = new TbSchedule();
        bean = bean.beanFromRequest(request);
        bean.setIsDelete(1);
        bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        TB_SCHEDULE_SERVICE.insertIntoTable(connection, bean);
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}