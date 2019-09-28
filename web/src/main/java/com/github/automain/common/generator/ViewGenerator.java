package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class ViewGenerator {

    public String generate(List<ColumnBean> columns, String prefix, String upperPrefix, String tableName, String upperTableName,
                           List<String> listCheck, List<String> addCheck, List<String> updateCheck, List<String> detailCheck,
                           List<String> keyColumns, List<String> sortCheck, boolean hasIsValid, boolean hasGlobalId, List<String> dictionaryColumnList) {
        String resultStr = "<template>\n    <div>";

        boolean hasAdd = !addCheck.isEmpty();
        boolean hasSearch = !keyColumns.isEmpty();

        if (hasAdd || hasSearch || hasIsValid) {
            resultStr += getBoxCard(columns, hasAdd, hasIsValid, tableName, keyColumns);
        }
        return resultStr;
    }

    private String getBoxCard(List<ColumnBean> columns, boolean hasAdd, boolean hasIsValid, String tableName, List<String> keyColumns) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        String addBlock = hasAdd ? "                <el-form-item>\n                    <el-button type=\"success\" icon=\"el-icon-plus\" @click=\"handleClear(true)\">添加</el-button>\n                </el-form-item>\n" : "";
        String deleteBlock = hasIsValid ? "                <el-form-item>\n                    <el-button type=\"warning\" icon=\"el-icon-delete\" @click=\"handleDelete\">删除</el-button>\n                </el-form-item>\n": "";
        StringBuilder searchBlock = new StringBuilder();
        if (!keyColumns.isEmpty()) {
            for (ColumnBean column : columns) {
                String columnName = column.getColumnName();
                if (keyColumns.contains(columnName)) {
                    String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
                    String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
                    String columnComment = column.getColumnComment();
                    if (CommonGenerator.checkTimeTypeColumn(column)) {
                        searchBlock.append("                <el-form-item label=\"").append(columnComment)
                                .append(":\">\n                    <el-date-picker v-model=\"").append(lowerColumnName)
                                .append("Range\" type=\"datetimerange\" start-placeholder=\"开始时间\" end-placeholder=\"结束时间\" :onPick=\"handle")
                                .append(upperColumnName).append("Range()\" value-format=\"timestamp\"></el-date-picker>\n                </el-form-item>\n");
                    } else {
                        searchBlock.append("                <el-form-item label=\"")
                                .append(columnComment).append(":\">\n                    <el-input v-model=\"")
                                .append(lowerTableName).append("VO.").append(lowerColumnName)
                                .append("\" placeholder=\"").append(columnComment)
                                .append("\"></el-input>\n                </el-form-item>\n");
                    }
                }
            }
            searchBlock.append("                <el-form-item>\n                    <el-button type=\"primary\" icon=\"el-icon-search\" @click=\"handleSearch()\">查询</el-button>\n                </el-form-item>\n");
        }
        return "\n        <el-card class=\"box-card\">\n            <el-form :inline=\"true\" :model=\"" +
                lowerTableName + "VO\" size=\"mini\">\n" + addBlock + deleteBlock + searchBlock +
                "            </el-form>\n        </el-card>";
    }
}
