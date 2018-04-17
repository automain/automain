package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ColumnListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = getString("tableName", request);
        List<String> columnList = TB_DICTIONARY_SERVICE.selectColumnNameList(connection, tableName);
        request.setAttribute("columnList", columnList);
        return null;
    }
}
