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

public class TestController extends BaseController {

    @RequestUri("/test/list")
    public JsonResponse testList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null) {
            PageBean<Test> pageBean = TEST_SERVICE.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson("success", pageBean);
        }
        return JsonResponse.getFailedJson("failed");
    }

    @RequestUri("/test/insertOrUpdate")
    public JsonResponse testInsertOrUpdate(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (bean != null) {
            bean.setUpdateTime(DateUtil.getNow());
            if (bean.getGid() != null) {
                TEST_SERVICE.updateTableByGid(connection, bean, false);
            } else {
                bean.setCreateTime(bean.getUpdateTime());
                TEST_SERVICE.insertIntoTable(connection, bean);
            }
//            if (bean.getId() != null) {
//                TEST_SERVICE.updateTableById(connection, bean, false);
//            } else {
//                bean.setCreateTime(bean.getUpdateTime());
//                TEST_SERVICE.insertIntoTable(connection, bean);
//            }
            return JsonResponse.getSuccessJson("success");
        }
        return JsonResponse.getFailedJson("failed");
    }

    @RequestUri("/test/detail")
    public JsonResponse testDetail(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Test bean = getRequestParam(request, Test.class);
        if (bean != null && bean.getGid() != null) {
            Test detail = TEST_SERVICE.selectTableByGid(connection, bean);
            return JsonResponse.getSuccessJson("success", detail);
        }
//        if (bean != null && bean.getId() != null) {
//            Test detail = TEST_SERVICE.selectTableById(connection, bean);
//            return JsonResponse.getSuccessJson("success", detail);
//        }
        return JsonResponse.getFailedJson("failed");
    }

    @RequestUri("/test/delete")
    public JsonResponse testDelete(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TestVO vo = getRequestParam(request, TestVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            TEST_SERVICE.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson("success");
        }
//        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
//            TEST_SERVICE.softDeleteTableByIdList(connection, vo.getIdList());
//            return JsonResponse.getSuccessJson("success");
//        }
        return JsonResponse.getFailedJson("failed");
    }
    
}
