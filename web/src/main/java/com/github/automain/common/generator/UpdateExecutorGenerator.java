package com.github.automain.common.generator;

public class UpdateExecutorGenerator extends CommonGenerator {

    public String generate(String tableName, String executorPrefix, boolean hasUpdate) {
        if (tableName == null || !hasUpdate) {
            return null;
        }
        try {
            String resultStr = "";

            resultStr += getImportHead();

            resultStr += getClassHead(executorPrefix);

            resultStr += getDoAction(tableName);
            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead() {
        return "import com.github.automain.common.BaseExecutor;\n" +
                "import com.github.automain.common.annotation.RequestUrl;\n" +
                "import com.github.fastjdbc.bean.ConnectionBean;\n" +
                "import redis.clients.jedis.Jedis;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n\n";
    }

    private String getClassHead(String executorPrefix) {
        return "@RequestUrl(\"/" + executorPrefix.replace("_", "/") + "/update\")\n"
                + "public class " + convertToJavaName(executorPrefix, true) + "UpdateExecutor extends BaseExecutor {";
    }

    private String getDoAction(String tableName) {
        String upperTableName = convertToJavaName(tableName, true);
        String serviceName = tableName + "_service";
        serviceName = serviceName.toUpperCase();
        return "\n\n    @Override\n" +
                "    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n" +
                "        " + upperTableName + " bean = new " + upperTableName + "();\n" +
                "        bean = bean.beanFromRequest(request);\n" +
                "        " + serviceName + ".updateTable(connection, bean, false);\n" +
                "        setJsonResult(request, CODE_SUCCESS, \"编辑成功\");\n" +
                "        return null;\n" +
                "    }";
    }

}
