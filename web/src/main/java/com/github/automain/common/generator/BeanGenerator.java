package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanGenerator extends CommonGenerator {

    public String generate(List<ColumnBean> columns, String tableName, String upperTableName) {
        try {
            String resultStr = "";

            resultStr += getImportHead(columns);

            resultStr += getClassHead(upperTableName);

            resultStr += getProperties(columns);

            resultStr += getGetterSetter(columns, upperTableName);

            resultStr += getTableName(tableName);

            resultStr += getColumnMap(columns);

            resultStr += getBeanFromResultSet(columns, upperTableName);

            resultStr += getToString(columns, upperTableName);

            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead(List<ColumnBean> columns) {
        Set<String> timeTypeSet = new HashSet<String>();
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            if ("Timestamp".equals(dataType)) {
                timeTypeSet.add("import java.sql.Timestamp;\n");
            }
            if ("Time".equals(dataType)) {
                timeTypeSet.add("import java.sql.Time;\n");
            }
            if ("Date".equals(dataType)) {
                timeTypeSet.add("import java.util.Date;\n");
            }
        }
        StringBuilder timeTypeImport = new StringBuilder();
        for (String s : timeTypeSet) {
            timeTypeImport.append(s);
        }
        return "import com.github.fastjdbc.common.BaseBean;\n\n" +
                "import java.sql.ResultSet;\n" +
                "import java.sql.SQLException;\n" + timeTypeImport.toString() +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + " implements BaseBean<" + upperTableName + "> {";
    }

    private String getProperties(List<ColumnBean> columns) {
        StringBuilder resultStr = new StringBuilder();
        for (ColumnBean column : columns) {
            String lowerColumnName = convertToJavaName(column.getColumnName(), false);
            resultStr.append("\n    // ").append(column.getColumnComment())
                    .append("\n    private ").append(column.getDataType()).append(" ").append(lowerColumnName).append(";");
        }
        return resultStr.toString();
    }

    private String getGetterSetter(List<ColumnBean> columns, String upperTableName) {
        StringBuilder resultStr = new StringBuilder();
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = convertToJavaName(column.getColumnName(), false);
            resultStr.append("\n\n    public ").append(column.getDataType())
                    .append(" get").append(upperColumnName).append("() {\n        return ")
                    .append(lowerColumnName).append(";\n    }\n\n    public ")
                    .append(upperTableName).append(" set").append(upperColumnName)
                    .append("(").append(column.getDataType())
                    .append(" ").append(lowerColumnName)
                    .append(") {\n        this.").append(lowerColumnName)
                    .append(" = ").append(lowerColumnName)
                    .append(";\n        return this;\n    }");
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

    private String getColumnMap(List<ColumnBean> columns) {
        StringBuilder notNullBody = new StringBuilder();
        int size = 0;
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            notNullBody.append("\n        if (all || this.get").append(upperColumnName).append("() != null) {\n")
                    .append("            map.put(\"").append(column.getColumnName()).append("\", this.get")
                    .append(upperColumnName).append("());\n        }");
            size++;
        }
        return "\n\n    @Override\n" +
                "    public Map<String, Object> columnMap(boolean all) {\n" +
                "        Map<String, Object> map = new HashMap<String, Object>(" + Math.max(1, size) + ");" +
                notNullBody.toString() + "\n" +
                "        return map;\n" +
                "    }";
    }

    private String getBeanFromResultSet(List<ColumnBean> columns, String upperTableName) {
        StringBuilder setValueBody = new StringBuilder();
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            if ("Integer".equals(dataType)) {
                dataType = "Int";
            }
            if ("byte[]".equals(dataType)) {
                dataType = "Object";
            }
            setValueBody.append("                .set")
                    .append(convertToJavaName(column.getColumnName(), true))
                    .append("(rs.get").append(dataType)
                    .append("(\"").append(column.getColumnName()).append("\"))\n");
        }
        return "\n\n    @Override\n" +
                "    public " + upperTableName + " beanFromResultSet(ResultSet rs) throws SQLException {\n" +
                "        return new " + upperTableName + "()\n" +
                setValueBody + ";\n    }";
    }

    private String getToString(List<ColumnBean> columns, String upperTableName) {
        StringBuilder setValueBody = new StringBuilder();
        List<String> numberTypeList = List.of("BigDecimal", "Double", "Float", "Integer");
        boolean isFirst = true;
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            String columnName = convertToJavaName(column.getColumnName(), false);
            if (numberTypeList.contains(dataType)) {
                if (isFirst) {
                    setValueBody.append("                \"").append(columnName).append("=\" + ").append(columnName).append(" +\n");
                } else {
                    setValueBody.append("                \", ").append(columnName).append("=\" + ").append(columnName).append(" +\n");
                }
            } else {
                if (isFirst) {
                    setValueBody.append("                \"").append(columnName).append("='\" + ").append(columnName).append(" + '\\''").append(" +\n");
                } else {
                    setValueBody.append("                \", ").append(columnName).append("='\" + ").append(columnName).append(" + '\\''").append(" +\n");
                }
            }
            isFirst = false;
        }
        return "\n\n    @Override\n" +
                "    public String toString() {\n" +
                "        return \""+upperTableName+"{\" +\n" +
                setValueBody.toString() + "\n                '}';";
    }

}
