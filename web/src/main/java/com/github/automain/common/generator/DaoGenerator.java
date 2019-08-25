package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoGenerator {

    public String generate(List<ColumnBean> columns, List<String> keyColumns, String tableName, String upperTableName, boolean hasIsValid) {
        try {
            String resultStr = "";

            resultStr += getImportHead();

            resultStr += getClassHead(upperTableName);

            resultStr += getSelectTableForCustomPage(upperTableName);

            resultStr += getSetSearchCondition(tableName, upperTableName, columns, keyColumns, hasIsValid);

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
                "import com.github.fastjdbc.bean.PageParamBean;\n" +
                "import com.github.fastjdbc.common.BaseDao;\n\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Dao extends BaseDao<" + upperTableName + "> {";
    }

    private String getSelectTableForCustomPage(String upperTableName) {
        return "\n\n    @SuppressWarnings(\"unchecked\")\n" +
                "    public PageBean<" + upperTableName + "> selectTableForCustomPage(ConnectionBean connection, " +
                upperTableName + "VO bean) throws Exception {\n" +
                "        List<Object> countParamList = new ArrayList<Object>();\n" +
                "        List<Object> paramList = new ArrayList<Object>();\n" +
                "        String countSql = setSearchCondition(bean, countParamList, true);\n" +
                "        String sql = setSearchCondition(bean, paramList, false);\n" +
                "        PageParamBean<" + upperTableName + "> pageParamBean = new PageParamBean<" + upperTableName + ">()\n" +
                "                .setConnection(connection)\n" +
                "                .setBean(bean)\n" +
                "                .setCountSql(countSql)\n" +
                "                .setCountParamList(countParamList)\n" +
                "                .setSql(sql)\n" +
                "                .setParamList(paramList)\n" +
                "                .setPage(bean.getPage())\n" +
                "                .setSize(bean.getSize());\n" +
                "        return selectTableForPage(pageParamBean);\n" +
                "    }";
    }

    private String getSetSearchCondition(String tableName, String upperTableName, List<ColumnBean> columns, List<String> keyColumns, boolean deleteCheck) {
        String condition = "";
        List<ColumnBean> keyColumnList = new ArrayList<ColumnBean>();
        List<ColumnBean> generalColumns = new ArrayList<ColumnBean>();
        Map<String, ColumnBean> columnMap = new HashMap<String, ColumnBean>();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            if ("PRI".equals(column.getColumnKey())) {
                continue;
            }
            if (!keyColumns.contains(columnName)) {
                generalColumns.add(column);
            } else {
                columnMap.put(columnName, column);
            }
        }
        for (String keyColumn : keyColumns) {
            keyColumnList.add(columnMap.get(keyColumn));
        }
        condition += getCondition(keyColumnList);
        condition += getCondition(generalColumns);
        String firstCondition = deleteCheck ? "is_valid = 1" : "1 = 1";
        return "\n\n    private String setSearchCondition(" + upperTableName + "VO bean, List<Object> paramList, boolean isCountSql) {\n" +
                "        StringBuilder sql = new StringBuilder(\"SELECT \");\n" +
                "        sql.append(isCountSql ? \"COUNT(1)\" : \"*\").append(\" FROM " + tableName + " WHERE " + firstCondition + " \");\n" +
                condition +
                "        return sql.toString();\n" +
                "    }";
    }

    private String getCondition(List<ColumnBean> columns) {
        StringBuilder condition = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            if ("is_valid".equals(columnName)) {
                continue;
            }
            String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                condition.append("        if (bean.get").append(upperColumnName).append("() != null ) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" >= ? \");\n")
                        .append("            paramList.add(bean.get").append(upperColumnName).append("());\n")
                        .append("        }\n")
                        .append("        if (bean.get").append(upperColumnName).append("End() != null) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" < ?\");\n")
                        .append("            paramList.add(bean.get").append(upperColumnName).append("End());\n")
                        .append("        }\n");
            } else {
                condition.append("        if (bean.get").append(upperColumnName).append("() != null) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" = ?\");\n")
                        .append("            paramList.add(bean.get").append(upperColumnName).append("());\n")
                        .append("        }\n");
            }
        }
        return condition.toString();
    }

}
