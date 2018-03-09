package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbRequestMapping bean = new TbRequestMapping();
        bean = bean.beanFromRequest(request);
        TbRequestMapping exist = TB_REQUEST_MAPPING_SERVICE.selectTableByRequestUrl(connection, bean.getRequestUrl());
        if (exist != null) {
            setJsonResult(request, CODE_FAIL, "url已存在");
        } else {
            TB_REQUEST_MAPPING_SERVICE.insertIntoTable(connection, bean);
            setJsonResult(request, CODE_SUCCESS, "添加成功");
        }
        return null;
    }
}