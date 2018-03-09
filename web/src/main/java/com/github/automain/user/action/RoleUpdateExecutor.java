package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleUpdateExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbRole bean = new TbRole();
        bean = bean.beanFromRequest(request);
        TB_ROLE_SERVICE.updateTable(connection, bean);
        setJsonResult(request, CODE_SUCCESS, "编辑成功");
        return null;
    }
}