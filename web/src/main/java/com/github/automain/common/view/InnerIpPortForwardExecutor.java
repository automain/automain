package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbInnerIpPort;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/inner/ip/port/forward")
public class InnerIpPortForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "add":
                jspPath = "common/inner_ip_port_add";
                break;
            case "update":
            case "detail":
                Long innerId = getLong("innerId", request, 0L);
                TbInnerIpPort bean = TB_INNER_IP_PORT_SERVICE.selectTableById(connection, innerId);
                request.setAttribute("bean", bean);
                jspPath = "common/inner_ip_port_" + forwardType;
                break;
            default:
                jspPath = "common/inner_ip_port_tab";
        }
        return jspPath;
    }
}