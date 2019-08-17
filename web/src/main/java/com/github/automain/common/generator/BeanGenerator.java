package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class BeanGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName) {
        if (databaseName == null || tableName == null) {
            return null;
        }
        try {
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            ColumnBean priColumn = getPrimaryColumn(columns);
            String resultStr = "";

            resultStr += getImportHead(getTimeTypeImport(columns));

            resultStr += getClassHead(tableName);

            resultStr += getProperties(columns);

            resultStr += getGetterSetter(columns);

            resultStr += getTableName(tableName);

            resultStr += getPrimaryKey(priColumn);

            resultStr += getPrimaryValue(priColumn);

            resultStr += getColumnMap(priColumn, columns);

            resultStr += getBeanFromResultSet(columns, tableName);

            resultStr += getBeanFromRequest(columns, tableName);
            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead(String timeTypeImport) {
        return "import com.github.fastjdbc.common.BaseBean;\n" +
                "import com.github.fastjdbc.util.RequestUtil;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import java.sql.ResultSet;\n" +
                "import java.sql.SQLException;\n" + timeTypeImport +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n\n";
    }

    private String getClassHead(String tableName) {
        String upperTableName = convertToJavaName(tableName, true);
        return "public class " + upperTableName
                + " extends RequestUtil implements BaseBean<" + upperTableName + "> {";
    }

    private String getProperties(List<ColumnBean> columns) {
        StringBuilder resultStr = new StringBuilder();
        StringBuilder additional = new StringBuilder();
        StringBuilder additionalGetterSetter = new StringBuilder();
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = convertToJavaName(column.getColumnName(), false);
            resultStr.append("\n\n    // ").append(column.getColumnComment())
                    .append("\n    private ").append(column.getDataType()).append(" ").append(lowerColumnName).append(";");
            if (checkTimeTypeColumn(column)) {
                additional.append("\n    // ").append(column.getColumnComment())
                        .append("\n    private String ").append(lowerColumnName).append("Range;");
                additionalGetterSetter.append("\n\n    public String get").append(upperColumnName).append("Range() {\n")
                        .append("        return ").append(lowerColumnName).append("Range;\n")
                        .append("    }\n\n")
                        .append("    public void set").append(upperColumnName).append("Range(String ")
                        .append(lowerColumnName).append("Range) {\n")
                        .append("        this.").append(lowerColumnName).append("Range = ")
                        .append(lowerColumnName).append("Range;\n    }");
            }
        }
        resultStr.append("\n\n    // ========== additional column begin ==========\n")
                .append(additional).append(additionalGetterSetter)
                .append("\n\n    // ========== additional column end ==========");
        return resultStr.toString();
    }

    private String getGetterSetter(List<ColumnBean> columns) {
        StringBuilder resultStr = new StringBuilder();
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = convertToJavaName(column.getColumnName(), false);
            resultStr.append("\n\n    public ").append(column.getDataType())
                    .append(" get").append(upperColumnName).append("() {\n")
                    .append("        return ").append(lowerColumnName).append(";\n")
                    .append("    }\n\n")
                    .append("    public void set").append(upperColumnName)
                    .append("(").append(column.getDataType())
                    .append(" ").append(lowerColumnName)
                    .append(") {\n")
                    .append("        this.").append(lowerColumnName)
                    .append(" = ").append(lowerColumnName)
                    .append(";\n    }");
        }
        return resultStr.toString();
    }

    private String getTableName(String tableName) {
        String outTable = tableName == null ? null : "\"" + tableName + "\"";
        return "\n\n    @Override\n" +
                "    public String tableName() {\n" +
                "        return " + outTable + ";\n" +
                "    }";
    }

    private String getPrimaryKey(ColumnBean priColumn) {
        String outPrimaryKey = priColumn == null ? null : "\"" + priColumn.getColumnName() + "\"";
        return "\n\n    @Override\n" +
                "    public String primaryKey() {\n" +
                "        return " + outPrimaryKey + ";\n" +
                "    }";
    }

    private String getPrimaryValue(ColumnBean priColumn) {
        String outPrimaryValue = priColumn == null ? null : "this.get" + convertToJavaName(priColumn.getColumnName(), true) + "()";
        return "\n\n    @Override\n" +
                "    public Long primaryValue() {\n" +
                "        return " + outPrimaryValue + ";\n" +
                "    }";
    }

    private String getColumnMap(ColumnBean priColumn, List<ColumnBean> columns) {
        StringBuilder notNullBody = new StringBuilder();
        String priColumnName = priColumn == null ? "" : priColumn.getColumnName();
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            if (!priColumnName.equals(column.getColumnName())) {
                notNullBody.append("\n        if (all || this.get").append(upperColumnName).append("() != null) {\n")
                        .append("            map.put(\"").append(column.getColumnName()).append("\", this.get")
                        .append(upperColumnName).append("());\n        }");
            }
        }
        return "\n\n    @Override\n" +
                "    public Map<String, Object> columnMap(boolean all) {\n" +
                "        Map<String, Object> map = new HashMap<String, Object>();" +
                notNullBody.toString() + "\n" +
                "        return map;\n" +
                "    }";
    }

    public String getBeanFromResultSet(List<ColumnBean> columns, String tableName) {
        String upperTableName = convertToJavaName(tableName, true);
        StringBuilder setValueBody = new StringBuilder();
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            if ("Integer".equals(dataType)) {
                dataType = "Int";
            }
            if ("byte[]".equals(dataType)) {
                dataType = "Object";
            }
            setValueBody.append("        bean.set")
                    .append(convertToJavaName(column.getColumnName(), true))
                    .append("(rs.get").append(dataType)
                    .append("(\"").append(column.getColumnName()).append("\"));\n");
        }
        return "\n\n    @Override\n" +
                "    public " + upperTableName + " beanFromResultSet(ResultSet rs) throws SQLException {\n" +
                "        " + upperTableName + " bean = new " + upperTableName + "();\n" +
                setValueBody + "        return bean;\n    }";
    }

    public String getBeanFromRequest(List<ColumnBean> columns, String tableName) {
        String upperTableName = convertToJavaName(tableName, true);
        StringBuilder setValueBody = new StringBuilder();
        StringBuilder additionalBody = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String upperColumnName = convertToJavaName(columnName, true);
            String lowerColumnName = convertToJavaName(columnName, false);
            String dataType = column.getDataType();
            if ("byte[]".equals(dataType)) {
                continue;
            }
            if ("Integer".equals(dataType)) {
                dataType = "Int";
            }
            if (DELETE_LABEL_COLUMN_NAME.equals(columnName)) {
                setValueBody.append("        bean.set").append(upperColumnName)
                        .append("(get").append(dataType).append("(\"").append(lowerColumnName).append("\", request, 0));\n");
            } else if ("Timestamp".equals(dataType) || "Time".equals(dataType)){
                setValueBody.append("        bean.set").append(upperColumnName)
                        .append("(new Timestamp(getInt").append("(\"").append(lowerColumnName).append("\", request)) * 1000);\n");
            } else {
                setValueBody.append("        bean.set").append(upperColumnName)
                        .append("(get").append(dataType).append("(\"").append(lowerColumnName).append("\", request));\n");
            }
            if (checkTimeTypeColumn(column)) {
                additionalBody.append("        bean.set").append(upperColumnName)
                        .append("Range(getString(\"").append(lowerColumnName).append("Range\", request));\n");
            }
        }
        return "\n\n    @Override\n" +
                "    public " + upperTableName + " beanFromRequest(HttpServletRequest request) {\n" +
                "        " + upperTableName + " bean = new " + upperTableName + "();\n" +
                setValueBody.toString() + additionalBody.toString() +
                "        return bean;\n    }";
    }
}
