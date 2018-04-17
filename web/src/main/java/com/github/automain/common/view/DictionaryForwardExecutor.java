package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.bean.TbDictionary;
import com.github.automain.common.vo.DictionaryVO;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        Long parentId = getLong("parentId", request, 0L);
        request.setAttribute("parentId", parentId);
        switch (forwardType) {
            case "add":
                jspPath = "common/dictionary_add";
                TbDictionary parent = TB_DICTIONARY_SERVICE.selectTableById(connection, parentId);
                request.setAttribute("parent", parent);
                break;
            case "update":
                Long dictionaryId = getLong("dictionaryId", request, 0L);
                TbDictionary bean = TB_DICTIONARY_SERVICE.selectTableById(connection, dictionaryId);
                request.setAttribute("bean", bean);
                jspPath = "common/dictionary_" + forwardType;
                break;
            default:
                List<DictionaryVO> parentList = new ArrayList<DictionaryVO>();
                if (parentId.equals(0L)){
                    request.setAttribute("cite", "顶级");
                } else {
                    TbDictionary dictionary = TB_DICTIONARY_SERVICE.selectTableById(connection, parentId);
                    if (dictionary != null) {
                        request.setAttribute("cite", dictionary.getDictionaryName());
                        setBreadcrumb(connection, dictionary, parentList);
                        Collections.reverse(parentList);
                        request.setAttribute("parentList", parentList);
                    }
                }
                jspPath = "common/dictionary_tab";
        }
        List<String> tableNameList = TB_DICTIONARY_SERVICE.selectTableNameList(connection);
        request.setAttribute("tableNameList", tableNameList);
        return jspPath;
    }

    private void setBreadcrumb(ConnectionBean connection, TbDictionary dictionary, List<DictionaryVO> parentList) throws SQLException {
        DictionaryVO vo = new DictionaryVO();
        if (dictionary != null && !dictionary.getParentId().equals(0L)) {
            TbDictionary parent = TB_DICTIONARY_SERVICE.selectTableById(connection, dictionary.getParentId());
            if (parent != null){
                vo.setDictionaryName(parent.getDictionaryName());
                vo.setParentId(parent.getDictionaryId());
                parentList.add(vo);
            }
            setBreadcrumb(connection, parent, parentList);
        } else {
            vo.setDictionaryName("顶级");
            vo.setParentId(0L);
            parentList.add(vo);
        }
    }
}