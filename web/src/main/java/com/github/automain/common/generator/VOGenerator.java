package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class VOGenerator {

    public String generate(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId) {
        return getImportHead() + getClassHead(upperTableName) + getProperties(columns, upperTableName, hasGlobalId) + "\n}";
    }

    private String getImportHead() {
        return "import java.util.List;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "VO extends " + upperTableName + " {";
    }

    private String getProperties(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId) {
        StringBuilder properties = new StringBuilder();
        StringBuilder getterSetter = new StringBuilder();
        properties.append("\n\n    // 页码\n    private int page;\n    // 页大小\n    private int size;\n    // 排序字段\n    private String sortLabel;\n    // 排序顺序\n    private String sortOrder;");
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
            String upperColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), true);
            String lowerColumnName = CommonGenerator.convertToJavaName(column.getColumnName(), false);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                properties.append("\n    // ").append(column.getColumnComment())
                        .append("结束\n    private Integer ").append(lowerColumnName).append("End;");
                getterSetter.append("\n\n    public Integer get").append(upperColumnName)
                        .append("End() {\n        return ").append(lowerColumnName).
                        append("End;\n    }\n\n    public ").append(upperTableName).append("VO set")
                        .append(upperColumnName).append("End(Integer ").append(lowerColumnName)
                        .append("End) {\n        this.").append(lowerColumnName).append("End = ")
                        .append(lowerColumnName).append("End;\n        return this;\n    }");
            }
        }
        return properties.append(getterSetter).toString();
    }

}
