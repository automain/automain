package com.github.automain.controller;

import com.github.automain.bean.SysDictionary;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.util.DateUtil;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class DictionaryController extends BaseController {

    @RequestUri("/dictionaryAll")
    public JsonResponse dictionaryAll(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<DictionaryVO> allDictionary = SYS_DICTIONARY_DAO.selectAllDictionaryVO(connection);
        return JsonResponse.getSuccessJson(allDictionary);
    }

    @RequestUri(value = "/dictionaryList", slave = "slave1")
    public JsonResponse dictionaryList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionaryVO vo = getRequestParam(request, SysDictionaryVO.class);
        if (vo != null) {
            PageBean<SysDictionary> pageBean = SYS_DICTIONARY_DAO.selectTableForCustomPage(connection, vo);
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
            SYS_DICTIONARY_DAO.insertIntoTable(connection, bean);
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
            SYS_DICTIONARY_DAO.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/dictionaryDetail", slave = "slave1")
    public JsonResponse dictionaryDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = getRequestParam(request, SysDictionary.class);
        if (bean != null && bean.getId() != null) {
            SysDictionary detail = SYS_DICTIONARY_DAO.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }
}