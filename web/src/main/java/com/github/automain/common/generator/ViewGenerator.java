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
        boolean hasList = !listCheck.isEmpty();
        boolean hasUpdate = !updateCheck.isEmpty();
        boolean hasDetail = !detailCheck.isEmpty();

        if (hasAdd || hasSearch || hasIsValid) {
            resultStr += getBoxCard(columns, hasAdd, hasIsValid, tableName, keyColumns);
        }
        if (hasList) {
            resultStr += getList(columns, hasIsValid, tableName, listCheck, dictionaryColumnList, sortCheck, hasUpdate,hasDetail);
        }
        if (hasAdd) {
            resultStr += addDialog(columns, tableName, addCheck, dictionaryColumnList);
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

    private String getList(List<ColumnBean> columns, boolean hasIsValid, String tableName, List<String> listCheck, List<String> dictionaryColumnList, List<String> sortCheck, boolean hasUpdate, boolean hasDetail) {
        StringBuilder listBlock = new StringBuilder();
        String table = CommonGenerator.convertToJavaName(tableName, false);
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            String overflowTip = column.getIsTextArea() ? " show-overflow-tooltip" : "";
            if (listCheck.contains(columnName)) {
                if (CommonGenerator.checkTimeTypeColumn(column)) {
                    listBlock.append("            <el-table-column prop=\"").append(lowerColumnName)
                            .append("\" label=\"").append(columnComment)
                            .append("\" width=\"160\" :formatter=\"dateTimeFormatter\"");
                    if (sortCheck.contains(columnName)) {
                        listBlock.append(" sortable=\"custom\"");
                    }
                    listBlock.append("></el-table-column>\n");
                } else if (dictionaryColumnList.contains(columnName)) {
                    listBlock.append("            <el-table-column prop=\"").append(lowerColumnName)
                            .append("\" label=\"").append(columnComment)
                            .append("\" column-key=\"").append(lowerColumnName)
                            .append("\" :filters=\"").append(lowerColumnName)
                            .append("Map\">\n                <template slot-scope=\"scope\">\n                    {{scope.row.")
                            .append(lowerColumnName).append(" | dictionaryFilter(").append(lowerColumnName)
                            .append("Key)}}\n                </template>\n            </el-table-column>\n");
                } else {
                    listBlock.append("            <el-table-column prop=\"").append(lowerColumnName)
                            .append("\" label=\"").append(columnComment)
                            .append("\"").append(overflowTip).append("></el-table-column>\n");
                }
            }
        }
        StringBuilder operation = new StringBuilder();
        if (hasUpdate || hasDetail) {
            operation.append("            <el-table-column fixed=\"right\" label=\"操作\" width=\"100\">\n                <template slot-scope=\"scope\">\n");
            if (hasUpdate) {
                operation.append("                    <el-button @click=\"handleUpdate(scope.row)\" type=\"text\" size=\"small\">编辑</el-button>\n");
            }
            if (hasDetail) {
                operation.append("                    <el-button @click=\"handleDetail(scope.row)\" type=\"text\" size=\"small\">详情</el-button>\n");
            }
            operation.append("                </template>\n            </el-table-column>\n");
        }
        String deleteSelect = hasIsValid ? "            <el-table-column type=\"selection\" width=\"42\" fixed=\"left\"></el-table-column>\n" : "";
        return "\n        <el-table ref=\"multipleTable\" :data=\"pageBean.data\" tooltip-effect=\"dark\" :height=\"fullHeight\" @selection-change=\"selectToDelete\" @sort-change=\"handleSort\" @filter-change=\"handleFilterChange\">\n" +
                deleteSelect + listBlock + operation +
                "        </el-table>\n        <el-pagination @size-change=\"handleSizeChange\" @current-change=\"handlePageChange\" :page-sizes=\"[10, 20, 50, 100]\" :page-size=\"" +
                table + "VO.size\" layout=\"->, total, prev, pager, next, jumper, sizes\" :total=\"pageBean.total\"></el-pagination>\n";
    }

    private String addDialog(List<ColumnBean> columns, String tableName, List<String> addCheck, List<String> dictionaryColumnList) {
        return null;
    }
}
