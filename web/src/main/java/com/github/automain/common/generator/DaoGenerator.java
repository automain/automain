package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, boolean deleteCheck) {
        if (databaseName == null || tableName == null) {
            return null;
        }
        try {
            String upperTableName = convertToJavaName(tableName, true);
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            List<String> keyColumns = selectKeyColumnList(null, databaseName, tableName);
            String resultStr = "";

            resultStr += getImportHead();

            resultStr += getClassHead(upperTableName);

            resultStr += getSelectTableForCustomPage(upperTableName);

            resultStr += getSetSearchCondition(tableName, columns, keyColumns, deleteCheck);
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
                upperTableName + " bean, int page, int limit) throws Exception {\n" +
                "        List<Object> countParameterList = new ArrayList<Object>();\n" +
                "        List<Object> parameterList = new ArrayList<Object>();\n" +
                "        String countSql = setSearchCondition(bean, countParameterList, true);\n" +
                "        String sql = setSearchCondition(bean, parameterList, false);\n" +
                "        PageParameterBean<" + upperTableName + "> pageParameterBean = new PageParameterBean<" + upperTableName + ">();\n" +
                "        pageParameterBean.setConnection(connection);\n" +
                "        pageParameterBean.setBean(bean);\n" +
                "        pageParameterBean.setCountSql(countSql);\n" +
                "        pageParameterBean.setCountParameterList(countParameterList);\n" +
                "        pageParameterBean.setSql(sql);\n" +
                "        pageParameterBean.setParameterList(parameterList);\n" +
                "        pageParameterBean.setPage(page);\n" +
                "        pageParameterBean.setLimit(limit);\n" +
                "        return selectTableForPage(pageParameterBean);\n" +
                "    }";
    }

    private String getSetSearchCondition(String tableName, List<ColumnBean> columns, List<String> keyColumns, boolean deleteCheck) {
        String upperTableName = convertToJavaName(tableName, true);
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
        if (!keyColumnList.isEmpty()) {
            condition += getCondition(keyColumnList);
        }
        condition += getCondition(generalColumns);
        String firstCondition = deleteCheck ? "is_delete = 0" : "1 = 1";
        return "\n\n    private String setSearchCondition(" + upperTableName + " bean, List<Object> parameterList, boolean isCountSql) {\n" +
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
            if ("is_delete".equals(columnName)) {
                continue;
            }
            String upperColumnName = convertToJavaName(columnName, true);
            if (checkTimeTypeColumn(column)) {
                condition.append("        if (bean.get").append(upperColumnName).append("Range() != null) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" >= ? AND ")
                        .append(columnName).append(" <= ?\");\n").append("            setTimeRange(bean.get")
                        .append(upperColumnName).append("Range(), parameterList);\n").append("        }\n");
            } else {
                condition.append("        if (bean.get").append(upperColumnName).append("() != null) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" = ?\");\n")
                        .append("            parameterList.add(bean.get").append(upperColumnName).append("());\n")
                        .append("        }\n");
            }
        }
        return condition.toString();
    }
}
