package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.List;

public class DaoGenerator {

    public String generate(List<ColumnBean> columns, List<String> keyColumns, String tableName, String upperTableName, boolean hasIsValid, List<String> dictionaryColumnList, boolean hasGlobalId) {
        return getImportHead(dictionaryColumnList) + getClassHead(upperTableName) + getDefaultBean(upperTableName) + getBaseDaoFunction(upperTableName, hasIsValid, hasGlobalId) + getSelectTableForCustomPage(upperTableName) + getSetSearchCondition(tableName, upperTableName, columns, keyColumns, hasIsValid, dictionaryColumnList) + "\n}";
    }

    private String getImportHead(List<String> dictionaryColumnList) {
        String collectionUtilImport = dictionaryColumnList.isEmpty() ? "" : "import org.apache.commons.collections4.CollectionUtils;\n";
        return "import com.github.fastjdbc.PageBean;\n" +
                "import com.github.fastjdbc.PageParamBean;\n" +
                "import com.github.fastjdbc.BaseDao;\n" +
                collectionUtilImport +
                "import org.apache.commons.lang3.StringUtils;\n\n" +
                "import java.sql.Connection;\n" +
                "import java.sql.SQLException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "Dao extends BaseDao<" + upperTableName + "> {";
    }

    private String getDefaultBean(String upperTableName) {
        return "\n\n    private static final " + upperTableName + " DEFAULT_BEAN = new " + upperTableName + "();";
    }

    private String getBaseDaoFunction(String upperTableName, boolean hasIsValid, boolean hasGlobalId) {
        String updateTableByGid = hasGlobalId ? "\n\n    public int updateTableByGid(Connection connection, " + upperTableName + " bean, boolean all) throws SQLException {\n        return super.updateTableByGid(connection, bean, all);\n    }" : "";
        String updateTableByGidList = hasGlobalId ? "\n\n    public int updateTableByGidList(Connection connection, " + upperTableName + " bean, List<String> gidList, boolean all) throws SQLException {\n        return super.updateTableByGidList(connection, bean, gidList, all);\n    }" : "";
        String softDeleteTableById = hasIsValid ? "\n\n    public int softDeleteTableById(Connection connection, " + upperTableName + " bean) throws SQLException {\n        return super.softDeleteTableById(connection, bean);\n    }" : "";
        String softDeleteTableByGid = hasIsValid && hasGlobalId ? "\n\n    public int softDeleteTableByGid(Connection connection, " + upperTableName + " bean) throws SQLException {\n        return super.softDeleteTableByGid(connection, bean);\n    }" : "";
        String softDeleteTableByIdList = hasIsValid ? "\n\n    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {\n        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);\n    }" : "";
        String softDeleteTableByGidList = hasIsValid && hasGlobalId ? "\n\n    public int softDeleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {\n        return super.softDeleteTableByGidList(connection, DEFAULT_BEAN, gidList);\n    }" : "";
        String deleteTableByGid = hasGlobalId ? "\n\n    public int deleteTableByGid(Connection connection, " + upperTableName + " bean) throws SQLException {\n        return super.deleteTableByGid(connection, bean);\n    }" : "";
        String deleteTableByGidList = hasGlobalId ? "\n\n    public int deleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {\n        return super.deleteTableByGidList(connection, DEFAULT_BEAN, gidList);\n    }" : "";
        String selectTableByGid = hasGlobalId ? "\n\n    public " + upperTableName + " selectTableByGid(Connection connection, " + upperTableName + " bean) throws SQLException {\n        return super.selectTableByGid(connection, bean);\n    }" : "";
        String selectTableByGidList = hasGlobalId ? "\n\n    public List<" + upperTableName + "> selectTableByGidList(Connection connection, List<String> gidList) throws SQLException {\n        return super.selectTableByGidList(connection, DEFAULT_BEAN, gidList);\n    }" : "";
        return "\n\n    public int insertIntoTable(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.insertIntoTable(connection, bean);\n    }\n\n    public Integer insertIntoTableReturnId(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.insertIntoTableReturnId(connection, bean);\n    }\n\n    public int batchInsertIntoTable(Connection connection, List<" +
                upperTableName + "> list) throws SQLException {\n        return super.batchInsertIntoTable(connection, list);\n    }\n\n    public int updateTableById(Connection connection, " +
                upperTableName + " bean, boolean all) throws SQLException {\n        return super.updateTableById(connection, bean, all);\n    }" +
                updateTableByGid + "\n\n    public int updateTableByIdList(Connection connection, " +
                upperTableName + " bean, List<Integer> idList, boolean all) throws SQLException {\n        return super.updateTableByIdList(connection, bean, idList, all);\n    }" +
                updateTableByGidList + "\n\n    public int updateTable(Connection connection, " +
                upperTableName + " paramBean, " + upperTableName + " newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {\n        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);\n    }" +
                softDeleteTableById + softDeleteTableByGid + softDeleteTableByIdList + softDeleteTableByGidList + "\n\n    public int deleteTableById(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.deleteTableById(connection, bean);\n    }" +
                deleteTableByGid + "\n\n    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {\n        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);\n    }" +
                deleteTableByGidList + "\n\n    public int countTableByBean(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.countTableByBean(connection, bean);\n    }\n\n    public " + upperTableName + " selectTableById(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.selectTableById(connection, bean);\n    }" +
                selectTableByGid + "\n\n    public List<" + upperTableName + "> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {\n        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);\n    }" +
                selectTableByGidList + "\n\n    public " + upperTableName + " selectOneTableByBean(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.selectOneTableByBean(connection, bean);\n    }\n\n    public List<" + upperTableName + "> selectTableByBean(Connection connection, " +
                upperTableName + " bean) throws SQLException {\n        return super.selectTableByBean(connection, bean);\n    }\n\n    public List<" +
                upperTableName + "> selectAllTable(Connection connection) throws SQLException {\n        return super.selectAllTable(connection, DEFAULT_BEAN);\n    }\n\n    public PageBean<" +
                upperTableName + "> selectTableForPage(Connection connection, " + upperTableName + " bean, int page, int size) throws Exception {\n        return super.selectTableForPage(connection, bean, page, size);\n    }";
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
            } else if ("String".equals(column.getDataType())) {
                condition.append("        if (StringUtils.isNotBlank(bean.get").append(upperColumnName)
                        .append("())) {\n            sql.append(\" AND ")
                        .append(columnName).append(" = ?\");\n            paramList.add(bean.get").append(upperColumnName).append("());\n        }\n");
            } else {
                condition.append("        if (bean.get").append(upperColumnName)
                        .append("() != null) {\n            sql.append(\" AND ")
                        .append(columnName).append(" = ?\");\n            paramList.add(bean.get").append(upperColumnName).append("());\n        }\n");
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
