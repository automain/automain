package com.github.automain.common.container;

public class DictionaryContainer {

//    public static void reloadDictionary(Jedis jedis, ConnectionBean connection) {
//        try {
//            List<TbDictionary> validList = ServiceDaoContainer.TB_DICTIONARY_SERVICE.selectValidTable(connection);
//            String dictTableNameOffset = "";
//            String dictColumnNameOffset = "";
//            Long parentIdOffset = 0L;
//            List<DictionaryVO> voList = new ArrayList<DictionaryVO>();
//            for (TbDictionary dictionary : validList) {
//                String currDictTableName = dictionary.getDictTableName();
//                String currDictColumnName = dictionary.getDictColumnName();
//                Long currParentId = dictionary.getParentId();
//                if (!dictTableNameOffset.equals(currDictTableName) || !dictColumnNameOffset.equals(currDictColumnName) || !parentIdOffset.equals(currParentId)) {
//                    putInCache(jedis, dictTableNameOffset, dictColumnNameOffset, parentIdOffset, voList);
//                    dictTableNameOffset = currDictTableName;
//                    dictColumnNameOffset = currDictColumnName;
//                    parentIdOffset = currParentId;
//                    voList = new ArrayList<DictionaryVO>();
//                }
//                voList.add(new DictionaryVO(dictionary.getDictionaryName(), dictionary.getDictionaryValue(), parentIdOffset));
//            }
//            putInCache(jedis, dictTableNameOffset, dictColumnNameOffset, parentIdOffset, voList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void putInCache(Jedis jedis, String dictTableNameOffset, String dictColumnNameOffset, Long parentIdOffset, List<DictionaryVO> voList) {
//        String key = getKey(dictTableNameOffset, dictColumnNameOffset, parentIdOffset);
//        if (voList.size() > 0 && key != null) {
//            if (jedis != null) {
//                jedis.set(key, JSON.toJSONString(voList));
//            } else {
//                RedisUtil.setLocalCache(key, voList);
//            }
//        }
//    }
//
//    private static String getKey(String dictTableName, String dictColumnName, Long parentId) {
//        if (dictTableName == null || dictColumnName == null || parentId == null) {
//            return null;
//        }
//        return "dictionary_" + dictTableName + "_" + dictColumnName + parentId;
//    }
//
//    public static List<DictionaryVO> getDictionary(Jedis jedis, String dictTableName, String dictColumnName, Long parentId) {
//        String key = getKey(dictTableName, dictColumnName, parentId);
//        if (key == null) {
//            return null;
//        }
//        if (jedis != null) {
//            return JSON.parseArray(jedis.get(key), DictionaryVO.class);
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }
//
//    public static Map<String, String> getDictionaryMap(Jedis jedis, String dictTableName, String dictColumnName, Long parentId) {
//        List<DictionaryVO> list = getDictionary(jedis, dictTableName, dictColumnName, parentId);
//        if (list != null) {
//            Map<String, String> resultMap = new HashMap<String, String>(list.size());
//            for (DictionaryVO vo : list) {
//                resultMap.put(vo.getDictionaryValue(), vo.getDictionaryName());
//            }
//            return resultMap;
//        } else {
//            return null;
//        }
//    }
}
