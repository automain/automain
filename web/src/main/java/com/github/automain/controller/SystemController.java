package com.github.automain.controller;

import com.github.automain.bean.SysConfig;
import com.github.automain.bean.SysDictionary;
import com.github.automain.bean.SysFile;
import com.github.automain.bean.SysSchedule;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.dao.SysConfigDao;
import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.dao.SysFileDao;
import com.github.automain.dao.SysScheduleDao;
import com.github.automain.util.DateUtil;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysConfigVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.automain.vo.SysFileVO;
import com.github.automain.vo.SysScheduleVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class SystemController extends BaseController {

    // file
    @RequestUri(value = "/fileList", slave = "slave1")
    public JsonResponse fileList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysFileVO vo = getRequestParam(request, SysFileVO.class);
        if (vo != null) {
            PageBean<SysFile> pageBean = SysFileDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    // config
    @RequestUri(value = "/configList", slave = "slave1")
    public JsonResponse configList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysConfigVO vo = getRequestParam(request, SysConfigVO.class);
        if (vo != null) {
            PageBean<SysConfig> pageBean = SysConfigDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/configAdd")
    public JsonResponse configAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysConfig bean = getRequestParam(request, SysConfig.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysConfigDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysConfig bean) {
        return bean != null && bean.getConfigKey() != null && bean.getConfigValue() != null;
    }

    @RequestUri("/configUpdate")
    public JsonResponse configUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysConfig bean = getRequestParam(request, SysConfig.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysConfigDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/configDetail", slave = "slave1")
    public JsonResponse configDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysConfig bean = getRequestParam(request, SysConfig.class);
        if (bean != null && bean.getId() != null) {
            SysConfig detail = SysConfigDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/configDelete")
    public JsonResponse configDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysConfigVO vo = getRequestParam(request, SysConfigVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SysConfigDao.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    // dictionary
    @RequestUri("/dictionaryAll")
    public JsonResponse dictionaryAll(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<DictionaryVO> allDictionary = SysDictionaryDao.selectAllDictionaryVO(connection);
        return JsonResponse.getSuccessJson(allDictionary);
    }

    @RequestUri(value = "/dictionaryList", slave = "slave1")
    public JsonResponse dictionaryList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionaryVO vo = getRequestParam(request, SysDictionaryVO.class);
        if (vo != null) {
            PageBean<SysDictionary> pageBean = SysDictionaryDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/dictionaryAdd")
    public JsonResponse dictionaryAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = getRequestParam(request, SysDictionary.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysDictionaryDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysDictionary bean) {
        return bean != null && bean.getTableName() != null && bean.getColumnName() != null && bean.getDictionaryKey() != null && bean.getDictionaryValue() != null;
    }

    @RequestUri("/dictionaryUpdate")
    public JsonResponse dictionaryUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = getRequestParam(request, SysDictionary.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysDictionaryDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/dictionaryDetail", slave = "slave1")
    public JsonResponse dictionaryDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = getRequestParam(request, SysDictionary.class);
        if (bean != null && bean.getId() != null) {
            SysDictionary detail = SysDictionaryDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    // schedule
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
        return bean != null && bean.getScheduleName() != null && bean.getScheduleUrl() != null && bean.getStartExecuteTime() != null && bean.getPeriod() != null;
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
