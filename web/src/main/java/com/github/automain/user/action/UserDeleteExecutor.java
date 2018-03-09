package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TB_USER_SERVICE.softDeleteTableByIdList(connection,getLongValues("deleteCheck", request));
        setJsonResult(request, CODE_SUCCESS, "删除成功");
        return null;
    }
}