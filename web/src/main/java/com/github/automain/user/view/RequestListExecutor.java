package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbRequestMapping bean = new TbRequestMapping();
        bean = bean.beanFromRequest(request);
        PageBean<TbRequestMapping> pageBean = TB_REQUEST_MAPPING_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/request_list";
    }
}