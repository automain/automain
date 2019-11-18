package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class VOGenerator {

    public String generate(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId, List<String> dictionaryColumnList) {
        return getImportHead(upperTableName) + getClassHead(upperTableName) + getProperties(columns, upperTableName, hasGlobalId, dictionaryColumnList) + "\n}";
    }

    private String getImportHead(String upperTableName) {
        return "package com.github.automain.vo;\n\n" +
                "import com.github.automain.bean." + upperTableName + ";\n\n" +
                "import java.util.List;\n\n";
    }

    private String getClassHead(String upperTableName) {
        return "public class " + upperTableName + "VO extends " + upperTableName + " {";
    }

    private String getProperties(List<ColumnBean> columns, String upperTableName, boolean hasGlobalId, List<String> dictionaryColumnList) {
        StringBuilder properties = new StringBuilder();
        StringBuilder getterSetter = new StringBuilder();
        StringBuilder toString = new StringBuilder();
        properties.append("\n\n    // 页码\n    private int page;\n    // 页大小\n    private int size;\n    // 排序字段\n    private String sortLabel;\n    // 排序顺序\n    private String sortOrder;");

        getterSetter.append("\n\n    public int getPage() {\n        return page;\n    }\n\n")
                .append("    public ").append(upperTableName)
                .append("VO setPage(int page) {\n        this.page = page;\n        return this;\n    }")
                .append("\n\n    public int getSize() {\n        return size;\n    }\n\n")
                .append("    public ").append(upperTableName)
                .append("VO setSize(int size) {\n        this.size = size;\n        return this;\n    }")
                .append("\n\n    public String getSortLabel() {\n        return sortLabel;\n    }\n\n    public ").append(upperTableName)
                .append("VO setSortLabel(String sortLabel) {\n        this.sortLabel = sortLabel;\n        return this;\n    }\n\n    public String getSortOrder() {\n        return sortOrder;\n    }\n\n    public ")
                .append(upperTableName).append("VO setSortOrder(String sortOrder) {\n        this.sortOrder = sortOrder;\n        return this;\n    }");

        toString.append("\n\n    @Override\n    public String toString() {\n        return \"").append(upperTableName)
                .append("VO{\" +\n                \"page=\" + page +\n                \", size=\" + size +\n                \", sortLabel='\" + sortLabel + '\\'' +\n                \", sortOrder='\" + sortOrder + '\\'' +");
        if (hasGlobalId) {
            properties.append("\n    // 删除用GID集合\n    private List<String> gidList;");
            getterSetter.append("\n\n    public List<String> getGidList() {\n        return gidList;\n    }\n\n")
                    .append("    public ").append(upperTableName)
                    .append("VO setGidList(List<String> gidList) {\n        this.gidList = gidList;\n        return this;\n    }");
            toString.append("\n                \", gidList=\" + gidList +");
        } else {
            properties.append("\n    // 删除用ID集合\n    private List<Integer> idList;");
            getterSetter.append("\n\n    public List<Integer> getIdList() {\n        return idList;\n    }\n\n")
                    .append("    public ").append(upperTableName)
                    .append("VO setIdList(List<Integer> idList) {\n        this.idList = idList;\n        return this;\n    }");
            toString.append("\n                \", idList=\" + idList +");
        }
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                properties.append("\n    // ").append(column.getColumnComment())
                        .append("结束\n    private Integer ").append(lowerColumnName).append("End;");
                getterSetter.append("\n\n    public Integer get").append(upperColumnName)
                        .append("End() {\n        return ").append(lowerColumnName).
                        append("End;\n    }\n\n    public ").append(upperTableName).append("VO set")
                        .append(upperColumnName).append("End(Integer ").append(lowerColumnName)
                        .append("End) {\n        this.").append(lowerColumnName).append("End = ")
                        .append(lowerColumnName).append("End;\n        return this;\n    }");
                toString.append("\n                \", ").append(lowerColumnName).append("End=\" + ").append(lowerColumnName).append("End +");
            }
            if (dictionaryColumnList.contains(columnName)) {
                properties.append("\n    // ").append(column.getColumnComment())
                        .append("集合\n    private List<Integer> ").append(lowerColumnName).append("List;");
                getterSetter.append("\n\n    public List<Integer> get").append(upperColumnName)
                        .append("List() {\n        return ").append(lowerColumnName).
                        append("List;\n    }\n\n    public ").append(upperTableName).append("VO set")
                        .append(upperColumnName).append("List(List<Integer> ").append(lowerColumnName)
                        .append("List) {\n        this.").append(lowerColumnName).append("List = ")
                        .append(lowerColumnName).append("List;\n        return this;\n    }");
                toString.append("\n                \", ").append(lowerColumnName).append("List=\" + ").append(lowerColumnName).append("List +");
            }
        }
        toString.append("\n                '}';\n    }");
        return properties.append(getterSetter).append(toString).toString();
    }

}
