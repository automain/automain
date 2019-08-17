package com.github.automain.dao;

import com.github.automain.bean.TbSchedule;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbScheduleDao extends BaseDao<TbSchedule> {

    @SuppressWarnings("unchecked")
    public PageBean<TbSchedule> selectTableForCustomPage(ConnectionBean connection, TbSchedule bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbSchedule> pageParameterBean = new PageParameterBean<TbSchedule>();
        pageParameterBean.setConnection(connection);
        pageParameterBean.setBean(bean);
        pageParameterBean.setCountSql(countSql);
        pageParameterBean.setCountParameterList(countParameterList);
        pageParameterBean.setSql(sql);
        pageParameterBean.setParameterList(parameterList);
        pageParameterBean.setPage(page);
        pageParameterBean.setLimit(limit);
        return selectTableForPage(pageParameterBean);
    }

    private String setSearchCondition(TbSchedule bean, List<Object> parameterList, boolean isCountSql) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_schedule WHERE is_delete = 0 ");
        if (bean.getScheduleUrl() != null) {
            sql.append(" AND schedule_url = ?");
            parameterList.add(bean.getScheduleUrl());
        }
        if (bean.getScheduleName() != null) {
            sql.append(" AND schedule_name = ?");
            parameterList.add(bean.getScheduleName());
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