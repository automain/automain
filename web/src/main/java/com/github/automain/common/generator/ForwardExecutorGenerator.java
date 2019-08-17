package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class ForwardExecutorGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String[] dictionaryCheck, String executorPrefix) {
        if (tableName == null) {
            return null;
        }
        try {
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            ColumnBean priColumn = getPrimaryColumn(columns);
            String priColumnName = convertToJavaName(priColumn.getColumnName(), false);
            String resultStr = "";

            resultStr += getImportHead(dictionaryCheck);

            resultStr += getClassHead(executorPrefix);

            resultStr += getDoAction(tableName, dictionaryCheck, executorPrefix, priColumnName);
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
                "import redis.clients.jedis.Jedis;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n" +
                "import java.util.List;\n\n";
    }

    private String getClassHead(String executorPrefix) {
        return "@RequestUrl(\"/" + executorPrefix.replace("_", "/") + "/forward\")\n"
                + "public class " + convertToJavaName(executorPrefix, true) + "ForwardExecutor extends BaseExecutor {";
    }

    private String getDoAction(String tableName, String[] dictionaryCheck, String executorPrefix, String priColumnName) {
        StringBuilder dictionary = new StringBuilder();
        if (dictionaryCheck != null) {
            for (String columnName : dictionaryCheck) {
                String lowerColumnName = convertToJavaName(columnName, false);
                dictionary.append("        request.setAttribute(\"").append(lowerColumnName).append("VOList\", DictionaryContainer.getDictionary(jedis, \"").append(tableName).append("\", \"").append(columnName).append("\", 0L));\n");
            }
        }
        String upperTableName = convertToJavaName(tableName, true);
        String serviceName = tableName + "_service";
        serviceName = serviceName.toUpperCase();
        return "\n\n    @Override\n" +
                "    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n" +
                "        String forwardType = getString(\"forwardType\", request, \"\");\n" +
                "        String jspPath = null;\n" +
                "        switch (forwardType) {\n" +
                "            case \"add\":\n" +
                "                jspPath = \"" + executorPrefix + "/" + executorPrefix + "_add\";\n" +
                "                break;\n" +
                "            case \"update\":\n" +
                "            case \"detail\":\n" +
                "                Long " + priColumnName + " = getLong(\"" + priColumnName + "\", request, 0L);\n" +
                "                " + upperTableName + " bean = " + serviceName + ".selectTableById(connection, " + priColumnName + ");\n" +
                "                request.setAttribute(\"bean\", bean);\n" +
                "                jspPath = \"" + executorPrefix + "/" + executorPrefix + "_\" + forwardType;\n" +
                "                break;\n" +
                "            default:\n" +
                "                jspPath = \"" + executorPrefix + "/" + executorPrefix + "_tab\";\n" +
                "        }\n" +
                dictionary +
                "        return jspPath;\n" +
                "    }";
    }
}
