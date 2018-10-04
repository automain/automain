package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/subtable/add")
public class SubTableAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mainTableName = getString("mainTableName", request);
        String subTableTime = getString("subTableTime", request);
        String subTableName = mainTableName + "_" + subTableTime.replace("-", "");
        try {
            TB_DICTIONARY_SERVICE.createSubTable(connection, mainTableName, subTableName);
            setJsonResult(request, CODE_SUCCESS, "创建成功");
        } catch (Exception e) {
            setJsonResult(request, CODE_FAIL, "创建失败");
        }
        return null;
    }
}
