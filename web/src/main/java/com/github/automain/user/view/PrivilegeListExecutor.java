package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/privilege/list")
public class PrivilegeListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbPrivilege bean = new TbPrivilege();
        bean = bean.beanFromRequest(request);
        PageBean<TbPrivilege> pageBean = TB_PRIVILEGE_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/privilege_list";
    }
}