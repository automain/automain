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

import java.util.UUID;

public class TestController extends BaseController {

    @RequestUri(value = "/testList", slave = "slave1")
    public JsonResponse testList(TestVO vo) throws Exception {
        if (vo != null) {
            PageBean<Test> pageBean = TestDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testAdd")
    public JsonResponse testAdd(Test bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            bean.setGid(UUID.randomUUID().toString());
            TestDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(Test bean) {
        return bean != null && bean.getTestName() != null && bean.getTestDictionary() != null;
    }

    @RequestUri("/testUpdate")
    public JsonResponse testUpdate(Test bean) throws Exception {
        if (checkValid(bean) && bean.getGid() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            TestDao.updateTableByGid(bean, false);
//            TestDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/testDetail", slave = "slave1")
    public JsonResponse testDetail(Test bean) throws Exception {
        if (bean != null && bean.getGid() != null) {
            Test detail = TestDao.selectTableByGid(bean);
            return JsonResponse.getSuccessJson(detail);
        }
//        if (bean != null && bean.getId() != null) {
//            Test detail = TestDao.selectTableById(bean);
//            return JsonResponse.getSuccessJson("success", detail);
//        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/testDelete")
    public JsonResponse testDelete(TestVO vo) throws Exception {
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            TestDao.softDeleteTableByGidList(vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
//        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
//            TestDao.softDeleteTableByIdList(vo.getIdList());
//            return JsonResponse.getSuccessJson("success");
//        }
        return JsonResponse.getFailedJson();
    }
}
