package com.github.automain.schedule;

import com.github.automain.common.BaseExecutor;
import com.github.automain.util.HTTPUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DBStatusSchedule extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return HTTPUtil.checkInnerIp(request);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
