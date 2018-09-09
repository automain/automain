package com.github.automain.common.dao;

import com.github.automain.common.bean.TbSchedule;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbScheduleDao extends BaseDao<TbSchedule> {

    @SuppressWarnings("unchecked")
    public PageBean<TbSchedule> selectTableForCustomPage(ConnectionBean connection, TbSchedule bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbSchedule bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_schedule WHERE 1 = 1 ");
        if (bean.getScheduleName() != null) {
            sql.append(" AND schedule_name LIKE ?");
            parameterList.add("%" + bean.getScheduleName() + "%");
        }
        if (bean.getScheduleUrl() != null) {
            sql.append(" AND schedule_url = ?");
            parameterList.add(bean.getScheduleUrl());
        }
        if (bean.getStartExecuteTimeRange() != null) {
            sql.append(" AND start_execute_time >= ? AND start_execute_time <= ?");
            setTimeRange(bean.getStartExecuteTimeRange(), parameterList);
        }
        if (bean.getDelayTime() != null) {
            sql.append(" AND delay_time = ?");
            parameterList.add(bean.getDelayTime());
        }
        if (bean.getLastExecuteTimeRange() != null) {
            sql.append(" AND last_execute_time >= ? AND last_execute_time <= ?");
            setTimeRange(bean.getLastExecuteTimeRange(), parameterList);
        }
        if (bean.getUpdateTimeRange() != null) {
            sql.append(" AND update_time >= ? AND update_time <= ?");
            setTimeRange(bean.getUpdateTimeRange(), parameterList);
        }
        return sql.toString();
    }
}