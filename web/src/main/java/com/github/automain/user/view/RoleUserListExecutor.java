package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleUserListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser bean = new TbUser();
        bean = bean.beanFromRequest(request);
        Long roleId = getLong("roleId", request, 0L);
        PageBean<TbUser> pageBean = TB_USER_SERVICE.selectTableForUserRole(connection, bean, request, roleId);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/role_user_list";
    }
}