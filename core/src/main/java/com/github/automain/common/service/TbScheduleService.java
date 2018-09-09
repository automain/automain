package com.github.automain.common.service;

import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.dao.TbScheduleDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TbScheduleService extends BaseService<TbSchedule, TbScheduleDao> {

    public TbScheduleService(TbSchedule bean, TbScheduleDao dao) {
        super(bean, dao);
    }

    public PageBean<TbSchedule> selectTableForCustomPage(ConnectionBean connection, TbSchedule bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public void refreseLastExecuteTime(ConnectionBean connection, String url) throws SQLException {
        TbSchedule bean = new TbSchedule();
        bean.setScheduleUrl(url);
        TbSchedule schedule = selectOneTableByBean(connection, bean);
        schedule.setLastExecuteTime(new Timestamp(System.currentTimeMillis()));
        updateTable(connection, schedule, false);
    }
}