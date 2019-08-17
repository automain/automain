package com.github.automain.common.generator;

public class ListExecutorGenerator extends CommonGenerator {

    public String generate(String tableName, String executorPrefix, String[] dictionaryCheck) {
        if (tableName == null) {
            return null;
        }
        try {
            String resultStr = "";

            resultStr += getImportHead(dictionaryCheck);

            resultStr += getClassHead(executorPrefix);

            resultStr += getDoAction(tableName, executorPrefix, dictionaryCheck);
            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead(String[] dictionaryCheck) {
        String import1 = "";
        if (dictionaryCheck != null) {
            import1 = "import com.github.automain.common.container.DictionaryContainer;\n";
        }
        return "import com.github.automain.common.BaseExecutor;\n" +
                "import com.github.automain.common.annotation.RequestUrl;\n" +
                import1 +
                "import com.github.fastjdbc.bean.ConnectionBean;\n" +
                "import com.github.fastjdbc.bean.PageBean;\n" +
                "import redis.clients.jedis.Jedis;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n\n";
    }

    private String getClassHead(String executorPrefix) {
        return "@RequestUrl(\"/" + executorPrefix.replace("_", "/") + "/list\")\n"
                + "public class " + convertToJavaName(executorPrefix, true) + "ListExecutor extends BaseExecutor {";
    }

    private String getDoAction(String tableName, String executorPrefix, String[] dictionaryCheck) {
        String upperTableName = convertToJavaName(tableName, true);
        String serviceName = tableName + "_service";
        serviceName = serviceName.toUpperCase();
        StringBuilder dictionary = new StringBuilder();
        if (dictionaryCheck != null) {
            for (String columnName : dictionaryCheck) {
                String lowerColumnName = convertToJavaName(columnName, false);
                dictionary.append("        request.setAttribute(\"")
                        .append(lowerColumnName).append("Map\", DictionaryContainer.getDictionaryMap(jedis, \"")
                        .append(tableName).append("\", \"").append(columnName).append("\", 0L));\n");
            }
        }
        return "\n\n    @Override\n" +
                "    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n" +
                "        " + upperTableName + " bean = new " + upperTableName + "();\n" +
                "        bean = bean.beanFromRequest(request);\n" +
                "        PageBean<" + upperTableName + "> pageBean = " + serviceName + ".selectTableForCustomPage(connection, bean, request);\n" +
                "        request.setAttribute(PAGE_BEAN_PARAM, pageBean);\n" + dictionary +
                "        return \"" + executorPrefix + "/" + executorPrefix + "_list\";\n" +
                "    }";
    }
}
