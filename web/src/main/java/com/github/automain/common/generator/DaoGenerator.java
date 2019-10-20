package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.List;

public class DaoGenerator {

    public String generate(List<ColumnBean> columns, List<String> keyColumns, String tableName, String upperTableName, boolean hasIsValid, List<String> dictionaryColumnList) {
        return getImportHead(dictionaryColumnList) + getClassHead(upperTableName) + getSelectTableForCustomPage(upperTableName) + getSetSearchCondition(tableName, upperTableName, columns, keyColumns, hasIsValid, dictionaryColumnList) + "\n}";
    }

    private String getImportHead(List<String> dictionaryColumnList) {
        String collectionUtilImport = dictionaryColumnList.isEmpty() ? "\n" : "import org.apache.commons.collections4.CollectionUtils;\n\n";
        return "import com.github.fastjdbc.bean.PageBean;\n" +
                "import com.github.fastjdbc.bean.PageParamBean;\n" +
                "import com.github.fastjdbc.common.BaseDao;\n" +
                collectionUtilImport +
                "import java.sql.Connection;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Dao extends BaseDao<" + upperTableName + "> {";
    }

    private String getSelectTableForCustomPage(String upperTableName) {
        return "\n\n    @SuppressWarnings(\"unchecked\")\n" +
                "    public PageBean<" + upperTableName + "> selectTableForCustomPage(Connection connection, " +
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

    private String getSetSearchCondition(String tableName, String upperTableName, List<ColumnBean> columns, List<String> keyColumns, boolean deleteCheck, List<String> dictionaryColumnList) {
        String condition = "";
        List<ColumnBean> keyColumnList = new ArrayList<ColumnBean>();
        List<ColumnBean> dictionaryColumns = new ArrayList<ColumnBean>();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            if ("PRI".equals(column.getColumnKey())) {
                continue;
            }
            if (keyColumns.contains(columnName)) {
                keyColumnList.add(column);
            } else if (dictionaryColumnList.contains(columnName)) {
                dictionaryColumns.add(column);
            }
        }
        condition += getCondition(keyColumnList);
        condition += getDictionaryCondition(dictionaryColumns);
        String firstCondition = deleteCheck ? "is_valid = 1" : "1 = 1";
        return "\n\n    private String setSearchCondition(" + upperTableName +
                "VO bean, List<Object> paramList, boolean isCountSql) {\n        StringBuilder sql = new StringBuilder(\"SELECT \");\n        sql.append(isCountSql ? \"COUNT(1)\" : \"*\").append(\" FROM " +
                tableName + " WHERE " + firstCondition + "\");\n" + condition +
                "        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {\n            sql.append(\" ORDER BY \").append(bean.getSortLabel()).append(\"asc\".equalsIgnoreCase(bean.getSortOrder()) ? \" ASC\" : \" DESC\");\n        }\n        return sql.toString();\n    }";
    }

    private String getCondition(List<ColumnBean> columnList) {
        StringBuilder condition = new StringBuilder();
        for (ColumnBean column : columnList) {
            String columnName = column.getColumnName();
            if ("is_valid".equals(columnName)) {
                continue;
            }
            String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                condition.append("        if (bean.get").append(upperColumnName).append("() != null ) {\n")
                        .append("            sql.append(\" AND ").append(columnName).append(" >= ?\");\n")
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

    private String getDictionaryCondition(List<ColumnBean> columnList) {
        StringBuilder condition = new StringBuilder();
        for (ColumnBean column : columnList) {
            String columnName = column.getColumnName();
            String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
            condition.append("        if (CollectionUtils.isNotEmpty(bean.get").append(upperColumnName)
                    .append("List())) {\n            sql.append(\" AND ").append(columnName)
                    .append("\").append(makeInStr(bean.get").append(upperColumnName)
                    .append("List()));\n            paramList.addAll(bean.get")
                    .append(upperColumnName).append("List());\n        }\n");
        }
        return condition.toString();
    }

}
