package com.github.automain.controller;

import com.github.automain.bean.SysDictionary;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.util.DateUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.bean.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class DictionaryController implements ServiceContainer {

    @RequestUri("/dictionaryAll")
    public JsonResponse dictionaryAll(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<DictionaryVO> allDictionary = SYS_DICTIONARY_SERVICE.selectAllDictionaryVO(connection);
        return JsonResponse.getSuccessJson(allDictionary);
    }

    @RequestUri("/dictionaryList")
    public JsonResponse dictionaryList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionaryVO vo = SystemUtil.getRequestParam(request, SysDictionaryVO.class);
        if (vo != null) {
            PageBean<SysDictionary> pageBean = SYS_DICTIONARY_SERVICE.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/dictionaryAddOrUpdate")
    public JsonResponse dictionaryAddOrUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = SystemUtil.getRequestParam(request, SysDictionary.class);
        if (bean != null) {
            bean.setUpdateTime(DateUtil.getNow());
            if (bean.getId() != null) {
                SYS_DICTIONARY_SERVICE.updateTableById(connection, bean, false);
            } else {
                bean.setCreateTime(bean.getUpdateTime());
                SYS_DICTIONARY_SERVICE.insertIntoTable(connection, bean);
            }
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/dictionaryDetail")
    public JsonResponse dictionaryDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionary bean = SystemUtil.getRequestParam(request, SysDictionary.class);
        if (bean != null && bean.getId() != null) {
            SysDictionary detail = SYS_DICTIONARY_SERVICE.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/dictionaryDelete")
    public JsonResponse dictionaryDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysDictionaryVO vo = SystemUtil.getRequestParam(request, SysDictionaryVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SYS_DICTIONARY_SERVICE.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
}