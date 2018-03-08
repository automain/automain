package com.github.automain.common.container;

import com.github.automain.common.vo.DictionaryVO;
import com.github.automain.dictionary.bean.TbDictionary;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DictionaryContainer {

    private static Map<String, List<DictionaryVO>> DICTIONARY_MAP = null;

    static {
        reloadDictionary();
    }

    public static void reloadDictionary(){
        ConnectionBean connection = null;
        ConcurrentHashMap<String, List<DictionaryVO>> dictionaryMap = new ConcurrentHashMap<String, List<DictionaryVO>>();
        try {
            connection = ConnectionPool.getConnectionBean(null);
            List<TbDictionary> validList = ServiceContainer.TB_DICTIONARY_SERVICE.selectValidTable(connection);
            String dictTableNameOffset = "";
            String dictColumnNameOffset = "";
            Long parentIdOffset = 0L;
            List<DictionaryVO> voList = new ArrayList<DictionaryVO>();
            for (TbDictionary dictionary : validList) {
                String currDictTableName = dictionary.getDictTableName();
                String currDictColumnName = dictionary.getDictColumnName();
                Long currParentId = dictionary.getParentId();
                if (!dictTableNameOffset.equals(currDictTableName) || !dictColumnNameOffset.equals(currDictColumnName) || !parentIdOffset.equals(currParentId)){
                    putInMap(dictionaryMap, dictTableNameOffset, dictColumnNameOffset, parentIdOffset, voList);
                    dictTableNameOffset = currDictTableName;
                    dictColumnNameOffset = currDictColumnName;
                    parentIdOffset = currParentId;
                    voList = new ArrayList<DictionaryVO>();
                }
                voList.add(new DictionaryVO(dictionary.getDictionaryName(), dictionary.getDictionaryValue(), parentIdOffset));
            }
            putInMap(dictionaryMap, dictTableNameOffset, dictColumnNameOffset, parentIdOffset, voList);
            DICTIONARY_MAP = dictionaryMap;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void putInMap(ConcurrentHashMap<String, List<DictionaryVO>> dictionaryMap, String dictTableNameOffset, String dictColumnNameOffset, Long parentIdOffset, List<DictionaryVO> voList){
        if (voList.size() > 0){
            dictionaryMap.put(getKey(dictTableNameOffset, dictColumnNameOffset, parentIdOffset), voList);
        }
    }

    private static String getKey(String dictTableName, String dictColumnName, Long parentId){
        if (dictTableName == null || dictColumnName == null || parentId == null){
            return  null;
        }
        return "key_" + dictTableName + "_" + dictColumnName + parentId.toString();
    }

    public static List<DictionaryVO> getDictionary(String dictTableName, String dictColumnName, Long parentId){
        String key = getKey(dictTableName, dictColumnName, parentId);
        if (key == null){
            return null;
        }
        return DICTIONARY_MAP.get(key);
    }

    public static Map<String, String> getDictionaryMap(String dictTableName, String dictColumnName, Long parentId) {
        List<DictionaryVO> list = getDictionary(dictTableName, dictColumnName, parentId);
        if (list != null) {
            Map<String, String> resultMap = new HashMap<String, String>(list.size());
            for (DictionaryVO vo : list) {
                resultMap.put(vo.getDictionaryValue(), vo.getDictionaryName());
            }
            return resultMap;
        } else {
            return null;
        }
    }
}
