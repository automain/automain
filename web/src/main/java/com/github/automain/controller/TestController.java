package com.github.automain.controller;

import com.github.automain.bean.Test;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.util.DateUtil;
import com.github.automain.vo.TestVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class TestController extends BaseController {

    @RequestUri("/testList")
    public JsonResponse testList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null) {
            PageBean<Test> pageBean = TEST_SERVICE.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testAdd")
    public JsonResponse testAdd(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
//            bean.setGid(UUID.randomUUID().toString());
            TEST_SERVICE.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(Test bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getId() != null) && bean.getMoney() != null && bean.getRemark() != null && bean.getTestDictionary() != null && bean.getTestName() != null;
    }

    @RequestUri("/testUpdate")
    public JsonResponse testUpdate(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
//            TEST_SERVICE.updateTableByGid(connection, bean, false);
            TEST_SERVICE.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testDetail")
    public JsonResponse testDetail(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (bean != null && bean.getGid() != null) {
            Test detail = TEST_SERVICE.selectTableByGid(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
//        if (bean != null && bean.getId() != null) {
//            Test detail = TEST_SERVICE.selectTableById(connection, bean);
//            return JsonResponse.getSuccessJson("success", detail);
//        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testDelete")
    public JsonResponse testDelete(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            TEST_SERVICE.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
//        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
//            TEST_SERVICE.softDeleteTableByIdList(connection, vo.getIdList());
//            return JsonResponse.getSuccessJson("success");
//        }
        return JsonResponse.getFailedJson();
    }
}
