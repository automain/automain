package com.github.automain.controller;

import com.github.automain.bean.SysSchedule;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.dao.SysScheduleDao;
import com.github.automain.util.DateUtil;
import com.github.automain.vo.SysScheduleVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ScheduleController extends BaseController {

    @RequestUri(value = "/scheduleList", slave = "slave1")
    public JsonResponse scheduleList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysScheduleVO vo = getRequestParam(request, SysScheduleVO.class);
        if (vo != null) {
            PageBean<SysSchedule> pageBean = SysScheduleDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/scheduleAdd")
    public JsonResponse scheduleAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysSchedule bean = getRequestParam(request, SysSchedule.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysScheduleDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysSchedule bean) {
        return bean != null && bean.getScheduleName() != null && bean.getScheduleUrl() != null && bean.getStartExecuteTime() != null && bean.getDelayTime() != null;
    }

    @RequestUri("/scheduleUpdate")
    public JsonResponse scheduleUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysSchedule bean = getRequestParam(request, SysSchedule.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysScheduleDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/scheduleDetail", slave = "slave1")
    public JsonResponse scheduleDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysSchedule bean = getRequestParam(request, SysSchedule.class);
        if (bean != null && bean.getId() != null) {
            SysSchedule detail = SysScheduleDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/scheduleDelete")
    public JsonResponse scheduleDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysScheduleVO vo = getRequestParam(request, SysScheduleVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SysScheduleDao.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
}