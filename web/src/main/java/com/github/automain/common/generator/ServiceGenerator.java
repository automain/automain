package com.github.automain.common.generator;

public class ServiceGenerator extends CommonGenerator {

    public String generate(String upperTableName) {
        try {
            String resultStr = "";

            resultStr += getImportHead();

            resultStr += getClassHead(upperTableName);

            resultStr += getConstructor(upperTableName);

            resultStr += getSelectTableForCustomPage(upperTableName);

            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead() {
        return "import com.github.fastjdbc.bean.ConnectionBean;\n" +
                "import com.github.fastjdbc.bean.PageBean;\n" +
                "import com.github.fastjdbc.common.BaseService;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Service extends BaseService<" + upperTableName + ", " + upperTableName + "Dao> {";
    }

    private String getConstructor(String upperTableName) {
        return "\n\n    public " + upperTableName + "Service(" + upperTableName + " bean, " + upperTableName +
                "Dao dao) {\n        super(bean, dao);\n    }";
    }

    private String getSelectTableForCustomPage(String upperTableName) {
        return "\n\n    public PageBean<" + upperTableName + "> selectTableForCustomPage(ConnectionBean connection, " +
                upperTableName + " bean, int page, int size) throws Exception {\n" +
                "        return getDao().selectTableForCustomPage(connection, bean, page, size);\n" +
                "    }";
    }

}
