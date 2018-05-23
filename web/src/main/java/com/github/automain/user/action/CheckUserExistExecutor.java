package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/user/check/exist")
public class CheckUserExistExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = getString("userName", request);
        Long userId = getLong("userId", request, 0L);
        TbUser user = TB_USER_SERVICE.selectTableByUserName(connection, userName);
        if (user != null && !userId.equals(user.getUserId())) {
            setJsonResult(request, CODE_FAIL, "用户名已存在");
        }
        return null;
    }
}
