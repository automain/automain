package com.github.automain.controller;

import com.github.automain.bean.SysMenu;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.DateUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.MenuVO;
import com.github.automain.vo.SysMenuVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class MenuController implements ServiceDaoContainer {

    @RequestUri(value = "/menuList", slave = "slave1")
    public JsonResponse menuList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenuVO vo = SystemUtil.getRequestParam(request, SysMenuVO.class);
        if (vo != null) {
            PageBean<SysMenu> pageBean = SYS_MENU_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuAdd")
    public JsonResponse menuAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SYS_MENU_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysMenu bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getId() != null);
    }

    @RequestUri("/menuUpdate")
    public JsonResponse menuUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_MENU_DAO.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/menuDetail", slave = "slave1")
    public JsonResponse menuDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (bean != null && bean.getId() != null) {
            SysMenu detail = SYS_MENU_DAO.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuDelete")
    public JsonResponse menuDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenuVO vo = SystemUtil.getRequestParam(request, SysMenuVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SYS_MENU_DAO.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/authorityMenu", slave = "slave1")
    public JsonResponse authorityMenu(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<MenuVO> menuVOList = SYS_MENU_SERVICE.authorityMenu(connection);
        return JsonResponse.getSuccessJson(menuVOList);
    }

    @RequestUri(value = "/allValidMenu", slave = "slave1")
    public JsonResponse allValidMenu(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<IdNameVO> menuVOList = SYS_MENU_DAO.allValidMenu(connection);
        return JsonResponse.getSuccessJson(menuVOList);
    }
}