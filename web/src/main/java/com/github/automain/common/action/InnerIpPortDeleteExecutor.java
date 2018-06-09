package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/inner/ip/port/delete")
public class InnerIpPortDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TB_INNER_IP_PORT_SERVICE.softDeleteTableByIdList(connection, getLongValues("deleteCheck", request));
        setJsonResult(request, CODE_SUCCESS, "删除成功");
        return null;
    }
}