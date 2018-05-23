package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.common.bean.TbConfig;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/config/list")
public class ConfigListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbConfig bean = new TbConfig();
        bean = bean.beanFromRequest(request);
        PageBean<TbConfig> pageBean = TB_CONFIG_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "common/config_list";
    }
}