package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class VOGenerator extends CommonGenerator {

    public String generate(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId) {
        try {
            String resultStr = "";

            resultStr += getClassHead(upperTableName);

            resultStr += getProperties(columns, upperTableName, hasGlobalId);

            resultStr += "\n\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "VO extends " + upperTableName + " {";
    }

    private String getProperties(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId) {
        StringBuilder properties = new StringBuilder();
        StringBuilder getterSetter = new StringBuilder();
        properties.append("\n\n    // 页码\n    private int page;\n    // 页大小\n    private int size;");
        if (hasGlobalId) {
            properties.append("\n    // 删除用GID集合\n    private List<String> gidList;");
        } else {
            properties.append("\n    // 删除用ID集合\n    private List<Integer> idList;");
        }
        getterSetter.append("\n\n    public int getPage() {\n        return page;\n    }\n\n")
                .append("    public ").append(upperTableName)
                .append("VO setPage(int page) {\n        this.page = page;\n        return this;\n    }")
                .append("\n\n    public int getSize() {\n        return size;\n    }\n\n")
                .append("    public ").append(upperTableName)
                .append("VO setSize(int size) {\n        this.size = size;\n        return this;\n    }");
        if (hasGlobalId) {
            getterSetter.append("\n\n    public List<String> getGidList() {\n        return gidList;\n    }\n\n")
                    .append("    public ").append(upperTableName)
                    .append("VO setGidList(List<String> gidList) {\n        this.gidList = gidList;\n        return this;\n    }");
        } else {
            getterSetter.append("\n\n    public List<Integer> getIdList() {\n        return idList;\n    }\n\n")
                    .append("    public ").append(upperTableName)
                    .append("VO setIdList(List<Integer> idList) {\n        this.idList = idList;\n        return this;\n    }");
        }
        for (ColumnBean column : columns) {
            String upperColumnName = convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = convertToJavaName(column.getColumnName(), false);
            if (checkTimeTypeColumn(column)) {
                properties.append("\n    // ").append(column.getColumnComment())
                        .append("\n    private Integer ").append(lowerColumnName).append("End;");
                getterSetter.append("\n\n    public Integer get").append(upperColumnName)
                        .append("End() {\n        return ").append(lowerColumnName).
                        append("End;\n    }\n\n    public ").append(upperTableName).append(" set")
                        .append(upperColumnName).append("End(Integer ").append(lowerColumnName)
                        .append("End) {\n        this.").append(lowerColumnName).append("End = ")
                        .append(lowerColumnName).append("End;\n        return this;\n    }");
            }
        }
        return properties.append(getterSetter).toString();
    }

}
