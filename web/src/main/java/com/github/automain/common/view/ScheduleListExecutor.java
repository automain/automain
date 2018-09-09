package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.container.DictionaryContainer;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/schedule/list")
public class ScheduleListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbSchedule bean = new TbSchedule();
        bean = bean.beanFromRequest(request);
        PageBean<TbSchedule> pageBean = TB_SCHEDULE_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        request.setAttribute("delayUnitMap", DictionaryContainer.getDictionaryMap(jedis, "tb_schedule", "delay_unit", 0L));
        return "common/schedule_list";
    }
}