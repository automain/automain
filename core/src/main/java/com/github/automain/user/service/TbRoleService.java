package com.github.automain.user.service;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.bean.TbUserRole;
import com.github.automain.user.dao.TbRoleDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TbRoleService extends BaseService<TbRole, TbRoleDao> implements ServiceContainer {

    public TbRoleService(TbRole bean, TbRoleDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRole> selectTableForCustomPage(ConnectionBean connection, TbRole bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean,  pageFromRequest(request), limitFromRequest(request));
    }

    public PageBean<TbRole> selectTableForForRequest(ConnectionBean connection, TbRole bean, HttpServletRequest request, String requestUrl) throws Exception {
        PageBean<TbRole> pageBean = selectTableForCustomPage(connection, bean, request);
        List<TbRole> data = pageBean.getData();
        TbRoleRequestMapping roleRequestMappingParam = new TbRoleRequestMapping();
        roleRequestMappingParam.setRequestUrl(requestUrl);
        roleRequestMappingParam.setIsDelete(0);
        for (TbRole role : data) {
            roleRequestMappingParam.setRoleId(role.getRoleId());
            TbRoleRequestMapping roleRequestMapping = TB_ROLE_REQUEST_MAPPING_SERVICE.selectOneTableByBean(connection, roleRequestMappingParam);
            if (roleRequestMapping != null) {
                role.setHasRole(1);
            } else {
                role.setHasRole(0);
            }
        }
        return pageBean;
    }

    public PageBean<TbRole> selectTableForForUser(ConnectionBean connection, TbRole bean, HttpServletRequest request, Long userId) throws Exception {
        PageBean<TbRole> pageBean = selectTableForCustomPage(connection, bean, request);
        List<TbRole> data = pageBean.getData();
        TbUserRole userRoleParam = new TbUserRole();
        userRoleParam.setUserId(userId);
        userRoleParam.setIsDelete(0);
        for (TbRole role : data) {
            userRoleParam.setRoleId(role.getRoleId());
            TbUserRole userRole = TB_USER_ROLE_SERVICE.selectOneTableByBean(connection, userRoleParam);
            if (userRole != null) {
                role.setHasRole(1);
            } else {
                role.setHasRole(0);
            }
        }
        return pageBean;
    }

    public PageBean<TbRole> selectTableForForMenu(ConnectionBean connection, TbRole bean, HttpServletRequest request, Long menuId) throws Exception {
        PageBean<TbRole> pageBean = selectTableForCustomPage(connection, bean, request);
        List<TbRole> data = pageBean.getData();
        TbRoleMenu roleMenuParam = new TbRoleMenu();
        roleMenuParam.setMenuId(menuId);
        roleMenuParam.setIsDelete(0);
        for (TbRole role : data) {
            roleMenuParam.setRoleId(role.getRoleId());
            TbRoleMenu roleMenu = TB_ROLE_MENU_SERVICE.selectOneTableByBean(connection, roleMenuParam);
            if (roleMenu != null) {
                role.setHasRole(1);
            } else {
                role.setHasRole(0);
            }
        }
        return pageBean;
    }

}