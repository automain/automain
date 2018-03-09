package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbRole bean = new TbRole();
        bean = bean.beanFromRequest(request);
        TB_ROLE_SERVICE.insertIntoTable(connection, bean);
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}