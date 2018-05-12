package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.bean.TbDictionary;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DictionaryUpdateExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbDictionary bean = new TbDictionary();
        bean = bean.beanFromRequest(request);
        TB_DICTIONARY_SERVICE.updateTable(connection, bean, false);
        setJsonResult(request, CODE_SUCCESS, "编辑成功");
        return null;
    }
}