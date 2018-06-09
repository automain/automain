package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbInnerIpPort;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/inner/ip/port/update")
public class InnerIpPortUpdateExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbInnerIpPort bean = new TbInnerIpPort();
        bean = bean.beanFromRequest(request);
        TB_INNER_IP_PORT_SERVICE.updateTable(connection, bean, false);
        setJsonResult(request, CODE_SUCCESS, "编辑成功");
        return null;
    }
}