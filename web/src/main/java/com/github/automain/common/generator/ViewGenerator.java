package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class ViewGenerator {

    public String generate(List<ColumnBean> columns, String prefix, String upperPrefix, String tableName, String upperTableName,
                           List<String> listCheck, List<String> addCheck, List<String> updateCheck, List<String> detailCheck,
                           List<String> searchCheck, List<String> sortCheck, boolean hasIsValid, boolean hasGlobalId) {
        String resultStr = "<template>\n    <div>";

        boolean hasAdd = !addCheck.isEmpty();
        boolean hasSearch = !searchCheck.isEmpty();

        if (hasAdd || hasSearch || hasIsValid) {
            resultStr += getBoxCard(columns, hasAdd, hasIsValid, tableName, searchCheck);
        }
        return resultStr;
    }

    private String getBoxCard(List<ColumnBean> columns, boolean hasAdd, boolean hasIsValid, String tableName, List<String> searchCheck) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        String addBlock = hasAdd ? "                <el-form-item>\n                    <el-button type=\"success\" icon=\"el-icon-plus\" @click=\"handleClear(true)\">添加</el-button>\n                </el-form-item>\n" : "";
        String deleteBlock = hasIsValid ? "                <el-form-item>\n                    <el-button type=\"warning\" icon=\"el-icon-delete\" @click=\"handleDelete\">删除</el-button>\n                </el-form-item>\n": "";
        StringBuilder searchBlock = new StringBuilder();
        if (!searchCheck.isEmpty()) {
            for (ColumnBean column : columns) {
                if (searchCheck.contains(column.getColumnName())) {

                }
            }
        }
        return "\n        <el-card class=\"box-card\">\n            <el-form :inline=\"true\" :model=\""
                + lowerTableName + "VO\" size=\"mini\">\n" + addBlock + deleteBlock +
                "                <el-form-item label=\"测试名称:\">\n" +
                "                    <el-input v-model=\"testVO.testName\" placeholder=\"测试名称\"></el-input>\n" +
                "                </el-form-item>\n" +
                "                <el-form-item label=\"创建时间:\">\n" +
                "                    <el-date-picker v-model=\"createTimeRange\" type=\"datetimerange\" start-placeholder=\"开始时间\" end-placeholder=\"结束时间\" :onPick=\"handleCreateTimeRange()\" value-format=\"timestamp\"></el-date-picker>\n" +
                "                </el-form-item>\n" +
                "                <el-form-item>\n" +
                "                    <el-button type=\"primary\" icon=\"el-icon-search\" @click=\"handleSearch()\">查询</el-button>\n" +
                "                </el-form-item>\n" +
                "            </el-form>\n" +
                "        </el-card>";
    }
}
