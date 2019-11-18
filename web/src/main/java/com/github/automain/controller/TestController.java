package com.github.automain.controller;

import com.github.automain.bean.Test;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.dao.TestDao;
import com.github.automain.util.DateUtil;
import com.github.automain.vo.TestVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.UUID;

public class TestController extends BaseController {

    @RequestUri(value = "/testList", slave = "slave1")
    public JsonResponse testList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null) {
            PageBean<Test> pageBean = TestDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testAdd")
    public JsonResponse testAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            bean.setGid(UUID.randomUUID().toString());
            TestDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(Test bean) {
        return bean != null && bean.getTestName() != null && bean.getTestDictionary() != null;
    }

    @RequestUri("/testUpdate")
    public JsonResponse testUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (checkValid(bean) && bean.getGid() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            TestDao.updateTableByGid(connection, bean, false);
//            TestDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/testDetail", slave = "slave1")
    public JsonResponse testDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (bean != null && bean.getGid() != null) {
            Test detail = TestDao.selectTableByGid(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
//        if (bean != null && bean.getId() != null) {
//            Test detail = TestDao.selectTableById(connection, bean);
//            return JsonResponse.getSuccessJson("success", detail);
//        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testDelete")
    public JsonResponse testDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            TestDao.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
//        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
//            TestDao.softDeleteTableByIdList(connection, vo.getIdList());
//            return JsonResponse.getSuccessJson("success");
//        }
        return JsonResponse.getFailedJson();
    }
}
