package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbConfig;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@RequestUrl("/config/add")
public class ConfigAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbConfig bean = new TbConfig();
        bean = bean.beanFromRequest(request);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setCreateTime(now);
        bean.setUpdateTime(now);
        bean.setIsDelete(0);
        TB_CONFIG_SERVICE.insertIntoTable(connection, bean);
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}