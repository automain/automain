package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/menu/role/list")
public class MenuRoleListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbRole bean = new TbRole();
        bean = bean.beanFromRequest(request);
        Long menuId = getLong("menuId", request, 0L);
        PageBean<TbRole> pageBean = TB_ROLE_SERVICE.selectTableForForMenu(connection, bean, request, menuId);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/menu_role_list";
    }
}