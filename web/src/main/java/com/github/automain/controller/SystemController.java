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
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SystemController extends BaseController {

    // file
    @RequestUri(value = "/fileList", slave = "slave1")
    public JsonResponse fileList(SysFileVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysFile> pageBean = SysFileDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    // config
    @RequestUri(value = "/configList", slave = "slave1")
    public JsonResponse configList(SysConfigVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysConfig> pageBean = SysConfigDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/configAdd")
    public JsonResponse configAdd(SysConfig bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysConfigDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysConfig bean) {
        return bean != null && bean.getConfigKey() != null && bean.getConfigValue() != null;
    }

    @RequestUri("/configUpdate")
    public JsonResponse configUpdate(SysConfig bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysConfigDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/configDetail", slave = "slave1")
    public JsonResponse configDetail(SysConfig bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysConfig detail = SysConfigDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/configDelete")
    public JsonResponse configDelete(SysConfigVO vo) throws Exception {
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SysConfigDao.softDeleteTableByIdList(vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    // dictionary
    @RequestUri("/dictionaryAll")
    public JsonResponse dictionaryAll() throws Exception {
        List<DictionaryVO> allDictionary = SysDictionaryDao.selectAllDictionaryVO();
        return JsonResponse.getSuccessJson(allDictionary);
    }

    @RequestUri(value = "/dictionaryList", slave = "slave1")
    public JsonResponse dictionaryList(SysDictionaryVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysDictionary> pageBean = SysDictionaryDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/dictionaryAdd")
    public JsonResponse dictionaryAdd(SysDictionary bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysDictionaryDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysDictionary bean) {
        return bean != null && bean.getTableName() != null && bean.getColumnName() != null && bean.getDictionaryKey() != null && bean.getDictionaryValue() != null;
    }

    @RequestUri("/dictionaryUpdate")
    public JsonResponse dictionaryUpdate(SysDictionary bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysDictionaryDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/dictionaryDetail", slave = "slave1")
    public JsonResponse dictionaryDetail(SysDictionary bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysDictionary detail = SysDictionaryDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    // schedule
    @RequestUri(value = "/checkScheduleExist", slave = "slave1")
    public JsonResponse checkScheduleExist(HttpServletRequest request) throws Exception {
        String scheduleUrl = request.getParameter("scheduleUrl");
        String idStr = request.getParameter("id");
        Integer id = Integer.valueOf("null".equals(idStr) ? "0" : idStr);
        if (StringUtils.isNotBlank(scheduleUrl)) {
            if (SysScheduleDao.checkScheduleUrlUseable(scheduleUrl, id)) {
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/scheduleList", slave = "slave1")
    public JsonResponse scheduleList(SysScheduleVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysSchedule> pageBean = SysScheduleDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/scheduleAdd")
    public JsonResponse scheduleAdd(SysSchedule bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysScheduleDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysSchedule bean) {
        return bean != null && bean.getScheduleName() != null && bean.getScheduleUrl() != null && bean.getStartExecuteTime() != null && bean.getPeriod() != null;
    }

    @RequestUri("/scheduleUpdate")
    public JsonResponse scheduleUpdate(SysSchedule bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysScheduleDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/scheduleDetail", slave = "slave1")
    public JsonResponse scheduleDetail(SysSchedule bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysSchedule detail = SysScheduleDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/scheduleInValid")
    public JsonResponse scheduleInValid(HttpServletRequest request) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        SysScheduleDao.softDeleteTableById(new SysSchedule().setId(id));
        return JsonResponse.getSuccessJson();
    }

    @RequestUri("/scheduleValid")
    public JsonResponse scheduleValid(HttpServletRequest request) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        SysScheduleDao.updateTableById(new SysSchedule().setId(id).setIsValid(1), false);
        return JsonResponse.getSuccessJson();
    }
}
