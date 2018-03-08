package com.github.automain.dictionary.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.dictionary.bean.TbDictionary;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DictionaryDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Long> deleteCheck = getLongValues("deleteCheck", request);
        if (deleteCheck != null && !deleteCheck.isEmpty()) {
            TB_DICTIONARY_SERVICE.softDeleteTableByIdList(connection, deleteCheck);
            connection.closeReadUseWrite();
            Long dictionaryId = deleteCheck.get(0);
            TbDictionary dictionary = TB_DICTIONARY_SERVICE.selectTableById(connection, dictionaryId);
            if (dictionary != null) {
                Long parentId = dictionary.getParentId();
                if (!parentId.equals(0L)) {
                    TbDictionary bean = new TbDictionary();
                    bean.setParentId(parentId);
                    List<TbDictionary> parentChildren = TB_DICTIONARY_SERVICE.selectTableByBean(connection, bean);
                    if (parentChildren.isEmpty()) {
                        TbDictionary parent = new TbDictionary();
                        parent.setDictionaryId(parentId);
                        parent.setIsLeaf(1);
                        TB_DICTIONARY_SERVICE.updateTable(connection, parent);
                    }
                }
            }
        }
        setJsonResult(request, CODE_SUCCESS, "删除成功");
        return null;
    }
}