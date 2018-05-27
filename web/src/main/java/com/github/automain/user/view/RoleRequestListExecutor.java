package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.DispatcherController;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/role/request/list")
public class RoleRequestListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        String searchUrl = getString("requestUrl", request);
        PageBean<TbRoleRequestMapping> pageBean = TB_ROLE_REQUEST_MAPPING_SERVICE.selectTableForRole(connection, DispatcherController.getRequestUrlList(), request, roleId, searchUrl);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "user/role_request_list";
    }
}