package com.github.automain.dictionary.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.dictionary.bean.TbDictionary;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DictionaryListExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbDictionary bean = new TbDictionary();
        bean = bean.beanFromRequest(request);
        PageBean<TbDictionary> pageBean = TB_DICTIONARY_SERVICE.selectTableForCustomPage(connection, bean, request);
        request.setAttribute(PAGE_BEAN_PARAM, pageBean);
        return "dictionary/dictionary_list";
    }
}