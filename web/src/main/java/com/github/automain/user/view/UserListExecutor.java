package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/user/list")
public class UserListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser bean = new TbUser();
        bean = bean.beanFromRequest(request);
        PageBean<TbUser> pageBean = TB_USER_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/user_list";
    }
}