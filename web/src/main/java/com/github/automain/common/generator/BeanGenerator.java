package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class BeanGenerator {

    public String generate(List<ColumnBean> columns, String tableName, String upperTableName) {
        return getImportHead(columns) + getClassHead(upperTableName) + getProperties(columns)
                + getGetterSetter(columns, upperTableName) + getTableName(tableName)
                + getColumnMap(columns) + getBeanFromResultSet(columns, upperTableName)
                + getToString(columns, upperTableName) + "\n}";
    }

    private String getImportHead(List<ColumnBean> columns) {
        String bigDecimalType = "";
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            if ("BigDecimal".equals(dataType)) {
                bigDecimalType = "import java.math.BigDecimal;\n";
                break;
            }
        }
        return "package com.github.automain.bean;\n\n" +
                "import com.github.fastjdbc.BaseBean;\n\n" + bigDecimalType +
                "import java.sql.ResultSet;\n" +
                "import java.sql.SQLException;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + " implements BaseBean<" + upperTableName + "> {\n";
    }

    private String getProperties(List<ColumnBean> columns) {
        StringBuilder resultStr = new StringBuilder();
        for (ColumnBean column : columns) {
            String lowerColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), false);
            resultStr.append("\n    // ").append(column.getColumnComment())
                    .append("\n    private ").append(column.getDataType()).append(" ").append(lowerColumnName).append(";");
        }
        return resultStr.toString();
    }

    private String getGetterSetter(List<ColumnBean> columns, String upperTableName) {
        StringBuilder resultStr = new StringBuilder();
        for (ColumnBean column : columns) {
            String upperColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), false);
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
            String upperColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), true);
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
            setValueBody.append("\n                .set")
                    .append(CommonGenerator.convertToJavaName(column.getColumnName(), true))
                    .append("(rs.get").append(dataType)
                    .append("(\"").append(column.getColumnName()).append("\"))");
        }
        return "\n\n    @Override\n" +
                "    public " + upperTableName + " beanFromResultSet(ResultSet rs) throws SQLException {\n" +
                "        return new " + upperTableName + "()" +
                setValueBody + ";\n    }";
    }

    private String getToString(List<ColumnBean> columns, String upperTableName) {
        StringBuilder setValueBody = new StringBuilder();
        List<String> numberTypeList = List.of("BigDecimal", "Double", "Float", "Integer");
        boolean isFirst = true;
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            String columnName = CommonGenerator.convertToJavaName(column.getColumnName(), false);
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
                setValueBody.toString() + "                '}';\n    }";
    }

}
