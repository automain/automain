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
            resultStr += getList(columns, hasIsValid, tableName, listCheck, dictionaryColumnList, sortCheck, hasUpdate, hasDetail);
        }
        if (hasAdd) {
            resultStr += addDialog(columns, tableName, addCheck, dictionaryColumnList);
        }
        if (hasUpdate) {
            resultStr += updateDialog(columns, tableName, updateCheck, dictionaryColumnList);
        }
        if (hasDetail) {
            resultStr += detailDialog(columns, tableName, detailCheck, dictionaryColumnList);
        }
        resultStr += "\n    </div>\n</template>\n";
        resultStr += getScript(columns, tableName, keyColumns, addCheck, updateCheck, detailCheck, sortCheck, dictionaryColumnList, hasIsValid, hasGlobalId);
        return resultStr;
    }

    private String getBoxCard(List<ColumnBean> columns, boolean hasAdd, boolean hasIsValid, String tableName, List<String> keyColumns) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        String addBlock = hasAdd ? "                <el-form-item>\n                    <el-button type=\"success\" icon=\"el-icon-plus\" @click=\"handleAddShow\">添加</el-button>\n                </el-form-item>\n" : "";
        String deleteBlock = hasIsValid ? "                <el-form-item>\n                    <el-button type=\"warning\" icon=\"el-icon-delete\" @click=\"handleDelete\">删除</el-button>\n                </el-form-item>\n" : "";
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
                            .append("\"");
                    if (sortCheck.contains(columnName)) {
                        listBlock.append(" sortable=\"custom\"");
                    }
                    listBlock.append(overflowTip).append("></el-table-column>\n");
                }
            }
        }
        StringBuilder operation = new StringBuilder();
        if (hasUpdate || hasDetail) {
            operation.append("            <el-table-column fixed=\"right\" label=\"操作\" width=\"100\">\n                <template slot-scope=\"scope\">\n");
            if (hasUpdate) {
                operation.append("                    <el-button @click=\"handleUpdateShow(scope.row)\" type=\"text\" size=\"small\">编辑</el-button>\n");
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
                table + "VO.size\" layout=\"->, total, prev, pager, next, jumper, sizes\" :total=\"pageBean.total\"></el-pagination>";
    }

    private String addDialog(List<ColumnBean> columns, String tableName, List<String> addCheck, List<String> dictionaryColumnList) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        StringBuilder addBlock = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            if (addCheck.contains(columnName)) {
                if (CommonGenerator.checkTimeTypeColumn(column)) {
                    addBlock.append("                <el-form-item label=\"")
                            .append(columnComment).append(":\">\n                    <el-date-picker v-model=\"")
                            .append(lowerColumnName).append("Picker\" type=\"datetime\" placeholder=\"请选择")
                            .append(columnComment).append("\" value-format=\"timestamp\" @change=\"function(val){")
                            .append(lowerTableName).append(".").append(lowerColumnName)
                            .append(" = val / 1000}\"></el-date-picker>\n                </el-form-item>\n");
                } else if (dictionaryColumnList.contains(columnName)) {
                    addBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    <el-select v-model=\"").append(lowerTableName)
                            .append(".").append(lowerColumnName).append("\" placeholder=\"请选择").append(columnComment)
                            .append("\">\n                        <el-option v-for=\"item in ").append(lowerColumnName)
                            .append("Map\" :key=\"item.value\" :label=\"item.text\" :value=\"item.value\"></el-option>\n                    </el-select>\n                </el-form-item>\n");
                } else {
                    addBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    <el-input v-model=\"").append(lowerTableName)
                            .append(".").append(lowerColumnName).append("\"");
                    if (column.getIsTextArea()) {
                        addBlock.append(" type=\"textarea\"");
                    } else {
                        addBlock.append(" autocomplete=\"off\"");
                    }
                    addBlock.append("></el-input>\n                </el-form-item>\n");
                }
            }
        }
        return "\n        <el-dialog title=\"添加\" :visible.sync=\"addVisible\" class=\"add-update-dialog\">\n            <el-form :model=\"" +
                lowerTableName + "\" inline label-width=\"120px\" size=\"mini\">\n" + addBlock +
                "            </el-form>\n            <div slot=\"footer\" class=\"dialog-footer\">\n                <el-button @click=\"addVisible = false\">取消</el-button>\n                <el-button type=\"primary\" @click=\"handleAddUpdate('/" +
                lowerTableName + "Add')\">确定</el-button>\n            </div>\n        </el-dialog>";
    }

    private String updateDialog(List<ColumnBean> columns, String tableName, List<String> updateCheck, List<String> dictionaryColumnList) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        StringBuilder updateBlock = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            if (updateCheck.contains(columnName)) {
                if (CommonGenerator.checkTimeTypeColumn(column)) {
                    updateBlock.append("                <el-form-item label=\"")
                            .append(columnComment).append(":\">\n                    <el-date-picker v-model=\"")
                            .append(lowerColumnName).append("Picker\" type=\"datetime\" placeholder=\"请选择")
                            .append(columnComment).append("\" value-format=\"timestamp\" @change=\"function(val){")
                            .append(lowerTableName).append(".").append(lowerColumnName)
                            .append(" = val / 1000}\"></el-date-picker>\n                </el-form-item>\n");
                } else if (dictionaryColumnList.contains(columnName)) {
                    updateBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    <el-select v-model=\"").append(lowerTableName)
                            .append(".").append(lowerColumnName).append("\" placeholder=\"请选择").append(columnComment)
                            .append("\">\n                        <el-option v-for=\"item in ").append(lowerColumnName)
                            .append("Map\" :key=\"item.value\" :label=\"item.text\" :value=\"item.value\"></el-option>\n                    </el-select>\n                </el-form-item>\n");
                } else {
                    updateBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    <el-input v-model=\"").append(lowerTableName)
                            .append(".").append(lowerColumnName).append("\"");
                    if (column.getIsTextArea()) {
                        updateBlock.append(" type=\"textarea\"");
                    } else {
                        updateBlock.append(" autocomplete=\"off\"");
                    }
                    updateBlock.append("></el-input>\n                </el-form-item>\n");
                }
            }
        }
        return "\n        <el-dialog title=\"编辑\" :visible.sync=\"updateVisible\" class=\"add-update-dialog\">\n            <el-form :model=\"" +
                lowerTableName + "\" inline label-width=\"120px\" size=\"mini\">\n" + updateBlock +
                "            </el-form>\n            <div slot=\"footer\" class=\"dialog-footer\">\n                <el-button @click=\"updateVisible = false\">取消</el-button>\n                <el-button type=\"primary\" @click=\"handleAddUpdate('/" +
                lowerTableName + "Update')\">确定</el-button>\n            </div>\n        </el-dialog>";
    }

    private String detailDialog(List<ColumnBean> columns, String tableName, List<String> detailCheck, List<String> dictionaryColumnList) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        StringBuilder detailBlock = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            if (detailCheck.contains(columnName)) {
                if (CommonGenerator.checkTimeTypeColumn(column)) {
                    detailBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    {{").append(lowerTableName)
                            .append(".").append(lowerColumnName)
                            .append(" | dateTimeFilter}}\n                </el-form-item>\n");
                } else if (dictionaryColumnList.contains(columnName)) {
                    detailBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    {{").append(lowerTableName)
                            .append(".").append(lowerColumnName).append(" | dictionaryFilter(")
                            .append(lowerColumnName).append("Key)}}\n                </el-form-item>\n");
                } else {
                    detailBlock.append("                <el-form-item label=\"").append(columnComment)
                            .append(":\">\n                    {{").append(lowerTableName)
                            .append(".").append(lowerColumnName).append("}}\n                </el-form-item>\n");
                }
            }
        }
        return "\n        <el-dialog title=\"详情\" :visible.sync=\"detailVisible\">\n            <el-form inline label-width=\"120px\" size=\"mini\">\n" +
                detailBlock + "            </el-form>\n        </el-dialog>";
    }

    private String getScript(List<ColumnBean> columns, String tableName, List<String> keyColumns, List<String> addCheck, List<String> updateCheck, List<String> detailCheck, List<String> sortCheck, List<String> dictionaryColumnList, boolean hasIsValid, boolean hasGlobalId) {
        String lowerTableName = CommonGenerator.convertToJavaName(tableName, false);
        boolean hasAdd = !addCheck.isEmpty();
        boolean hasUpdate = !updateCheck.isEmpty();
        boolean hasDetail = !detailCheck.isEmpty();
        String addVisible = hasAdd ? "                addVisible: false,\n" : "";
        String updateVisible = hasUpdate ? "                updateVisible: false,\n" : "";
        String detailVisible = hasDetail ? "                detailVisible: false,\n" : "";
        String sortParam = !sortCheck.isEmpty() ? "                    sortLabel: null,\n                    sortOrder: null,\n" : "";
        String deleteParam = hasIsValid ? hasGlobalId ? "                    gidList: [],\n" : "                    idList: [],\n" : "";
        StringBuilder timeRangeBlock = new StringBuilder();
        StringBuilder timePickerBlock = new StringBuilder();
        StringBuilder timeSearchBlock = new StringBuilder();
        StringBuilder dictionarySearchBlock = new StringBuilder();
        StringBuilder searchBlock = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                if (keyColumns.contains(columnName)) {
                    timeRangeBlock.append("                ").append(lowerColumnName).append("Range: [],\n");
                    timeSearchBlock.append("                    ").append(lowerColumnName)
                            .append(": null,\n                    ").append(lowerColumnName).append("End: null,\n");
                }
                if (addCheck.contains(columnName) || updateCheck.contains(columnName)) {
                    timePickerBlock.append("                ").append(lowerColumnName).append("Picker: null,\n");
                }
            } else if (dictionaryColumnList.contains(columnName)) {
                dictionarySearchBlock.append("                    ").append(lowerColumnName).append("List: []\n");
            } else {
                if (keyColumns.contains(columnName)) {
                    searchBlock.append("                    ").append(lowerColumnName).append(": null,\n");
                }
            }
        }
        return "\n<script>\n    export default {\n        data() {\n            return {\n" +
                addVisible + updateVisible + detailVisible + timeRangeBlock.toString() + timePickerBlock.toString() +
                "                " + lowerTableName +
                "VO: {\n                    page: 1,\n                    size: 10,\n" + sortParam + deleteParam +
                timeSearchBlock.toString() + dictionarySearchBlock.toString() + searchBlock.toString() +
                "                },\n                pageBean: {\n                    page: 1,\n                    total: 0,\n                    data: [],\n                },\n                " +
                lowerTableName + ": {\n" +
                "                    gid: null,\n" +
                "                    money: null,\n" +
                "                    remark: null,\n" +
                "                    testName: null,\n" +
                "                    createTime: null,\n" +
                "                    testDictionary: null,\n" +
                "                },\n" +
                "                testDictionaryKey: \"test_test_dictionary\",\n" +
                "                testDictionaryMap: this.getDictionaryMap(\"test_test_dictionary\"),\n" +
                "            }\n" +
                "        },\n" +
                "        methods: {\n" +
                "            handleSizeChange(val) {\n" +
                "                this.testVO.size = val;\n" +
                "                this.handleSearch();\n" +
                "            },\n" +
                "            handlePageChange(val) {\n" +
                "                this.testVO.page = val;\n" +
                "                this.handleSearch();\n" +
                "            },\n" +
                "            handleSearch() {\n" +
                "                this.$axios.post(\"/testList\", this.testVO).then(response => {\n" +
                "                    let data = response.data;\n" +
                "                    if (data.status === 0) {\n" +
                "                        this.pageBean = data.data;\n" +
                "                    }\n" +
                "                });\n" +
                "            },\n" +
                "            handleCreateTimeRange() {\n" +
                "                if (this.createTimeRange) {\n" +
                "                    this.testVO.createTime = this.createTimeRange[0] / 1000;\n" +
                "                    this.testVO.createTimeEnd = this.createTimeRange[1] / 1000;\n" +
                "                } else {\n" +
                "                    this.testVO.createTime = null;\n" +
                "                    this.testVO.createTimeEnd = null;\n" +
                "                }\n" +
                "            },\n" +
                "            handleSort(data) {\n" +
                "                switch (data.prop) {\n" +
                "                    case \"createTime\":\n" +
                "                        this.testVO.sortLabel = \"create_time\";\n" +
                "                        break;\n" +
                "                }\n" +
                "                this.testVO.sortOrder = data.order ? data.order.replace(\"ending\", \"\") : null;\n" +
                "                this.handleSearch();\n" +
                "            },\n" +
                "            handleFilterChange(data) {\n" +
                "                this.testVO.testDictionaryList = data.testDictionary;\n" +
                "                this.handleSearch();\n" +
                "            },\n" +
                "            handleSetProperties(row) {\n" +
                "                this.test.gid = row.gid;\n" +
                "                this.$axios.post(\"/testDetail\", this.test).then(response => {\n" +
                "                    let data = response.data;\n" +
                "                    if (data.status === 0) {\n" +
                "                        this.test = data.data;\n" +
                "                    } else {\n" +
                "                        this.$message.error(\"操作失败\");\n" +
                "                    }\n" +
                "                });\n" +
                "            },\n" +
                "            handleUpdateShow(row) {\n" +
                "                this.handleSetProperties(row);\n" +
                "                this.createTimePicker = row.createTime * 1000;\n" +
                "                this.updateVisible = true;\n" +
                "            },\n" +
                "            handleDetail(row) {\n" +
                "                this.handleSetProperties(row);\n" +
                "                this.detailVisible = true;\n" +
                "            },\n" +
                "            handleAddShow() {\n" +
                "                this.handleClear();\n" +
                "                this.createTimePicker = null;\n" +
                "                this.addVisible = true;\n" +
                "            },\n" +
                "            handleClear() {\n" +
                "                for (let key in this.test) {\n" +
                "                    if (this.test.hasOwnProperty(key)) {\n" +
                "                        this.test[key] = null;\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            handleAddUpdate(uri) {\n" +
                "                this.$axios.post(uri, this.test).then(response => {\n" +
                "                    let data = response.data;\n" +
                "                    if (data.status === 0) {\n" +
                "                        this.$message.success(\"操作成功\");\n" +
                "                        this.handleClear();\n" +
                "                        this.handleSearch();\n" +
                "                        this.addVisible = false;\n" +
                "                        this.updateVisible = false;\n" +
                "                    } else {\n" +
                "                        this.$message.error(\"操作失败\");\n" +
                "                    }\n" +
                "                });\n" +
                "            },\n" +
                "            selectToDelete(val) {\n" +
                "                let gidList = [];\n" +
                "                for (let i = 0, size = val.length; i < size; i++) {\n" +
                "                    gidList.push(val[i].gid);\n" +
                "                }\n" +
                "                this.testVO.gidList = gidList;\n" +
                "            },\n" +
                "            handleDelete() {\n" +
                "                if (this.testVO.gidList.length > 0) {\n" +
                "                    this.$confirm(\"确定删除选中的数据?\", \"提示\", {type: \"warning\"}).then(() => {\n" +
                "                        this.$axios.post(\"/testDelete\", this.testVO).then(response => {\n" +
                "                            let data = response.data;\n" +
                "                            if (data.status === 0) {\n" +
                "                                this.$message.success(\"操作成功\");\n" +
                "                                this.handleSearch();\n" +
                "                            } else {\n" +
                "                                this.$message.error(\"操作失败\");\n" +
                "                            }\n" +
                "                        });\n" +
                "                    }).catch(() => {\n" +
                "                        this.$message.info(\"取消删除\");\n" +
                "                    });\n" +
                "                } else {\n" +
                "                    this.$message.warning(\"请选择要删除的数据\");\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        mounted() {\n" +
                "            this.handleSearch();\n" +
                "        },\n" +
                "        computed: {\n" +
                "            fullHeight() {\n" +
                "                return this.$store.state.fullHeight - 140;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "</script>";
    }
}
