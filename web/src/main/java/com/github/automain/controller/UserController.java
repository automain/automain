package com.github.automain.controller;

import com.github.automain.bean.SysUser;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.DateUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.vo.SysUserVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.UUID;

public class UserController implements ServiceDaoContainer {

    @RequestUri(value = "/userList", slave = "slave1")
    public JsonResponse userList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = SystemUtil.getRequestParam(request, SysUserVO.class);
        if (vo != null) {
            PageBean<SysUser> pageBean = SYS_USER_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userAdd")
    public JsonResponse userAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            bean.setGid(UUID.randomUUID().toString());
            SYS_USER_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysUser bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getGid() != null);
    }

    @RequestUri("/userUpdate")
    public JsonResponse userUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_USER_DAO.updateTableByGid(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userDetail", slave = "slave1")
    public JsonResponse userDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (bean != null && bean.getGid() != null) {
            SysUser detail = SYS_USER_DAO.selectTableByGid(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userDelete")
    public JsonResponse userDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = SystemUtil.getRequestParam(request, SysUserVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            SYS_USER_DAO.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
}