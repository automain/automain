package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/subtable/forward")
public class SubTableForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> tableNameList = TB_DICTIONARY_SERVICE.selectTableNameList(connection);
        request.setAttribute("tableNameList", tableNameList);
        return "common/sub_table";
    }
}
