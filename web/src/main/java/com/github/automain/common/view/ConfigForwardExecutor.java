package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.common.bean.TbConfig;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/config/forward")
public class ConfigForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "add":
                jspPath = "common/config_add";
                break;
            case "update":
            case "detail":
                Long configId = getLong("configId", request, 0L);
                TbConfig bean = TB_CONFIG_SERVICE.selectTableById(connection, configId);
                request.setAttribute("bean", bean);
                jspPath = "common/config_" + forwardType;
                break;
            default:
                jspPath = "common/config_tab";
        }
        return jspPath;
    }
}