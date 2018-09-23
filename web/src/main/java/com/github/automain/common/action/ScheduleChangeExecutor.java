package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@RequestUrl("/schedule/change")
public class ScheduleChangeExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long changeId = getLong("changeId", request);
        connection.closeReadUseWrite();
        TbSchedule schedule = TB_SCHEDULE_SERVICE.selectTableById(connection, changeId);
        if (schedule != null) {
            String msg = null;
            TbSchedule bean = new TbSchedule();
            bean.setScheduleId(changeId);
            bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (schedule.getIsDelete().equals(0)) {
                msg = "关闭成功";
                bean.setIsDelete(1);
            } else {
                msg = "开启成功";
                bean.setIsDelete(0);
            }
            TB_SCHEDULE_SERVICE.updateTable(connection, bean, false);
            SystemUtil.reloadSchedule(connection, jedis);
            setJsonResult(request, CODE_SUCCESS, msg);
        } else {
            setJsonResult(request, CODE_FAIL, "未找到记录");
        }
        return null;
    }
}