package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        Long requestMappingId = getLong("requestMappingId", request, 0L);
        request.setAttribute("requestMappingId", requestMappingId);
        switch (forwardType) {
            case "add":
                jspPath = "user/request_add";
                break;
            case "role":
                jspPath = "user/request_role";
                break;
            case "update":
                TbRequestMapping bean = TB_REQUEST_MAPPING_SERVICE.selectTableById(connection, requestMappingId);
                request.setAttribute("bean", bean);
                jspPath = "user/request_update";
                break;
            default:
                jspPath = "user/request_tab";
        }
        return jspPath;
    }
}