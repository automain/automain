package com.github.automain.user.service;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbUser;
import com.github.automain.user.bean.TbUserRole;
import com.github.automain.user.dao.TbUserDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbUserService extends BaseService<TbUser, TbUserDao> implements ServiceContainer {

    public TbUserService(TbUser bean, TbUserDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUser> selectTableForCustomPage(ConnectionBean connection, TbUser bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }

    public PageBean<TbUser> selectTableForUserRole(ConnectionBean connection, TbUser bean, HttpServletRequest request, Long roleId) throws Exception {
        PageBean<TbUser> pageBean = selectTableForCustomPage(connection, bean, request);
        List<TbUser> data = pageBean.getData();
        TbUserRole userRoleParam = new TbUserRole();
        userRoleParam.setRoleId(roleId);
        userRoleParam.setIsDelete(0);
        for (TbUser user : data) {
            userRoleParam.setUserId(user.getUserId());
            TbUserRole userRole = TB_USER_ROLE_SERVICE.selectOneTableByBean(connection, userRoleParam);
            if (userRole != null) {
                user.setHasRole(1);
            } else {
                user.setHasRole(0);
            }
        }
        return pageBean;
    }

    public TbUser selectTableByUserName(ConnectionBean connection, String userName) throws SQLException{
        TbUser bean = new TbUser();
        bean.setUserName(userName);
        return getDao().selectOneTableByBean(connection, bean);
    }
}