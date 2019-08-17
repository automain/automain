package com.github.automain.common.generator;

public class DeleteExecutorGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String executorPrefix) {
        if (databaseName == null || tableName == null) {
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
        return "@RequestUrl(\"/" + executorPrefix.replace("_", "/") + "/delete\")\n"
                + "public class " + convertToJavaName(executorPrefix, true) + "DeleteExecutor extends BaseExecutor {";
    }

    private String getDoAction(String tableName) {
        String serviceName = (tableName + "_service").toUpperCase();
        return "\n\n    @Override\n" +
                "    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n" +
                "        " + serviceName + ".softDeleteTableByIdList(connection, getLongValues(\"deleteCheck\", request));\n" +
                "        setJsonResult(request, CODE_SUCCESS, \"删除成功\");\n" +
                "        return null;\n" +
                "    }";
    }
}
