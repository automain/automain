package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.bean.TbDictionary;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DictionaryAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbDictionary bean = new TbDictionary();
        bean = bean.beanFromRequest(request);
        bean.setIsLeaf(1);
        TB_DICTIONARY_SERVICE.insertIntoTable(connection, bean);
        Long parentId = bean.getParentId();
        TbDictionary parent = TB_DICTIONARY_SERVICE.selectTableById(connection, parentId);
        if (parent != null){
            TbDictionary p = new TbDictionary();
            p.setDictionaryId(parent.getDictionaryId());
            p.setIsLeaf(0);
            TB_DICTIONARY_SERVICE.updateTable(connection, p);
        }
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}