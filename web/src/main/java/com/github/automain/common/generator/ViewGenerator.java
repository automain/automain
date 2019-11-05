package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ViewGenerator {

    public String generate(List<ColumnBean> columns, String tableName, List<String> listCheck, List<String> addCheck, List<String> updateCheck, List<String> detailCheck,
                           List<String> keyColumns, List<String> sortCheck, boolean hasIsValid, boolean hasGlobalId, List<String> dictionaryColumnList, String prefix) {
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
            resultStr += addDialog(columns, tableName, addCheck, dictionaryColumnList, prefix);
        }
        if (hasUpdate) {
            resultStr += updateDialog(columns, tableName, updateCheck, dictionaryColumnList, prefix);
        }
        if (hasDetail) {
            resultStr += detailDialog(columns, tableName, detailCheck, dictionaryColumnList);
        }
        resultStr += "\n    </div>\n</template>\n";
        resultStr += getScript(columns, tableName, keyColumns, addCheck, updateCheck, detailCheck, sortCheck, dictionaryColumnList, hasIsValid, hasGlobalId, prefix);
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
                if (keyColumns.contains(columnName) && !"id".equals(columnName) && !"gid".equals(columnName)) {
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
        String sortChange = CollectionUtils.isNotEmpty(sortCheck) ? " @sort-change=\"handleSort\"" : "";
        return "\n        <el-table ref=\"multipleTable\" :data=\"pageBean.data\" tooltip-effect=\"dark\" :height=\"fullHeight\" @selection-change=\"selectToDelete\"" +
                sortChange + " @filter-change=\"handleFilterChange\">\n" + deleteSelect + listBlock + operation +
                "        </el-table>\n        <el-pagination @size-change=\"handleSizeChange\" @current-change=\"handlePageChange\" :page-sizes=\"[10, 20, 50, 100]\" :page-size=\"" +
                table + "VO.size\" layout=\"->, total, prev, pager, next, jumper, sizes\" :total=\"pageBean.total\"></el-pagination>";
    }

    private String addDialog(List<ColumnBean> columns, String tableName, List<String> addCheck, List<String> dictionaryColumnList, String prefix) {
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
                lowerTableName + "\" ref=\"" + lowerTableName + "Add\" :rules=\"rules\" inline label-width=\"120px\" size=\"mini\">\n" + addBlock +
                "            </el-form>\n            <div slot=\"footer\" class=\"dialog-footer\">\n                <el-button @click=\"addVisible = false\">取消</el-button>\n                <el-button type=\"primary\" @click=\"handleAddUpdate('/" +
                prefix + "Add', '" + lowerTableName + "Add')\">确定</el-button>\n            </div>\n        </el-dialog>";
    }

    private String updateDialog(List<ColumnBean> columns, String tableName, List<String> updateCheck, List<String> dictionaryColumnList, String prefix) {
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
                lowerTableName + "\" ref=\"" + lowerTableName + "Update\" :rules=\"rules\" inline label-width=\"120px\" size=\"mini\">\n" + updateBlock +
                "            </el-form>\n            <div slot=\"footer\" class=\"dialog-footer\">\n                <el-button @click=\"updateVisible = false\">取消</el-button>\n                <el-button type=\"primary\" @click=\"handleAddUpdate('/" +
                prefix + "Update', '" + lowerTableName + "Update')\">确定</el-button>\n            </div>\n        </el-dialog>";
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

    private String getScript(List<ColumnBean> columns, String tableName, List<String> keyColumns, List<String> addCheck, List<String> updateCheck, List<String> detailCheck, List<String> sortCheck, List<String> dictionaryColumnList, boolean hasIsValid, boolean hasGlobalId, String prefix) {
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
        StringBuilder beanBlock = new StringBuilder();
        beanBlock.append(hasIsValid ? "                    gid: null,\n" : "                    id: null,\n");
        StringBuilder dictionaryMapBlock = new StringBuilder();
        StringBuilder rulesBlock = new StringBuilder();
        StringBuilder handleTimeRangeBlock = new StringBuilder();
        StringBuilder sortBlock = new StringBuilder();
        StringBuilder dictionaryFilterBlock = new StringBuilder();
        StringBuilder handlePropertyBlock = new StringBuilder();
        StringBuilder handleUpdateBlock = new StringBuilder();
        StringBuilder handleDetailBlock = new StringBuilder();
        StringBuilder addShowBlock = new StringBuilder();
        StringBuilder handleAddUpdateBlock = new StringBuilder();
        StringBuilder handleDeleteBlock = new StringBuilder();
        if (hasIsValid) {
            handleDeleteBlock.append("            selectToDelete(val) {\n");
            handleDeleteBlock.append(hasGlobalId ? "                let gidList = [];\n" : "                let idList = [];\n");
            handleDeleteBlock.append("                for (let i = 0, size = val.length; i < size; i++) {\n");
            handleDeleteBlock.append(hasGlobalId ? "                    gidList.push(val[i].gid);\n" : "                    idList.push(val[i].id);\n");
            handleDeleteBlock.append("                }\n                this.").append(lowerTableName).append("VO.");
            handleDeleteBlock.append(hasGlobalId ? "gidList = gidList;\n" : "idList = idList;\n");
            handleDeleteBlock.append("            },\n            handleDelete() {\n                if (this.").append(lowerTableName).append("VO.");
            handleDeleteBlock.append(hasGlobalId ? "gidList" : "idList");
            handleDeleteBlock.append(".length > 0) {\n                    this.$confirm(\"确定删除选中的数据?\", \"提示\", {type: \"warning\"}).then(() => {\n                        this.$axios.post(\"/")
                    .append(prefix).append("Delete\", this.").append(lowerTableName)
                    .append("VO).then(response => {\n                            let data = response.data;\n                            if (data.status === 0) {\n                                this.$message.success(\"操作成功\");\n                                this.handleSearch();\n                            } else {\n                                this.$message.error(\"操作失败\");\n                            }\n                        });\n                    }).catch(() => {\n                        this.$message.info(\"取消删除\");\n                    });\n                } else {\n                    this.$message.warning(\"请选择要删除的数据\");\n                }\n            }\n        },\n");
        }
        if (hasAdd || hasUpdate) {
            handleAddUpdateBlock.append("            handleAddUpdate(uri, formName) {\n                this.$refs[formName].validate((valid) => {\n                    if (valid) {\n                        this.$axios.post(uri, this.").append(lowerTableName)
                    .append(").then(response => {\n                            let data = response.data;\n                            if (data.status === 0) {\n                                this.$message.success(\"操作成功\");\n                                this.handleClear();\n                                this.handleSearch();\n");
            if (hasAdd) {
                addShowBlock.append("            handleAddShow() {\n                this.handleClear();\n                if (this.$refs['")
                        .append(lowerTableName).append("Add']) {\n                    this.$refs['").append(lowerTableName).append("Add'].resetFields();\n                }\n");
                handleAddUpdateBlock.append("                                this.addVisible = false;\n");
            }
            if (hasUpdate) {
                handleAddUpdateBlock.append("                                this.updateVisible = false;\n");
            }
            handleAddUpdateBlock.append("                            } else {\n                                this.$message.error(\"操作失败\");\n                            }\n                        });\n                    } else {\n                        return false;\n                    }\n                });\n            },\n");
        }
        if (hasDetail || hasUpdate) {
            handlePropertyBlock.append("            handleSetProperties(row) {\n                this.").append(lowerTableName);
            handlePropertyBlock.append(hasGlobalId ? ".gid = row.gid;\n" : ".id = row.id;\n");
            handlePropertyBlock.append("                this.$axios.post(\"/").append(prefix).append("Detail\", this.").append(lowerTableName)
                    .append(").then(response => {\n                    let data = response.data;\n                    if (data.status === 0) {\n                        this.")
                    .append(lowerTableName).append(" = data.data;\n                    } else {\n                        this.$message.error(\"操作失败\");\n                    }\n                });\n            },\n");
            if (hasUpdate) {
                handleUpdateBlock.append("            handleUpdateShow(row) {\n                this.handleSetProperties(row);\n");
            }
            if (hasDetail) {
                handleDetailBlock.append("            handleDetail(row) {\n                this.handleSetProperties(row);\n                this.detailVisible = true;\n            },\n");
            }
        }
        if (CollectionUtils.isNotEmpty(sortCheck)) {
            sortBlock.append("            handleSort(data) {\n                switch (data.prop) {\n");
        }
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = CommonGenerator.convertToJavaName(columnName, false);
            String upperColumnName = CommonGenerator.convertToJavaName(columnName, true);
            if (CommonGenerator.checkTimeTypeColumn(column)) {
                if (keyColumns.contains(columnName)) {
                    timeRangeBlock.append("                ").append(lowerColumnName).append("Range: [],\n");
                    timeSearchBlock.append("                    ").append(lowerColumnName)
                            .append(": null,\n                    ").append(lowerColumnName).append("End: null,\n");
                    handleTimeRangeBlock.append("            handle").append(upperColumnName)
                            .append("Range() {\n                if (this.").append(lowerColumnName)
                            .append("Range) {\n                    this.").append(lowerTableName)
                            .append("VO.").append(lowerColumnName).append(" = this.").append(lowerColumnName)
                            .append("Range[0] / 1000;\n                    this.").append(lowerTableName)
                            .append("VO.").append(lowerColumnName).append("End = this.").append(lowerColumnName)
                            .append("Range[1] / 1000;\n                } else {\n                    this.").append(lowerTableName)
                            .append("VO.").append(lowerColumnName).append(" = null;\n                    this.").append(lowerTableName)
                            .append("VO.").append(lowerColumnName).append("End = null;\n                }\n            },\n");
                }
                if (addCheck.contains(columnName) || updateCheck.contains(columnName)) {
                    timePickerBlock.append("                ").append(lowerColumnName).append("Picker: null,\n");
                    if (updateCheck.contains(columnName)) {
                        handleUpdateBlock.append("                this.").append(lowerColumnName).append("Picker = row.").append(lowerColumnName).append(" * 1000;\n");
                    }
                    if (addCheck.contains(columnName)) {
                        addShowBlock.append("                this.").append(lowerColumnName).append("Picker = null;\n");
                    }
                }
            } else {
                if (keyColumns.contains(columnName) && !"id".equals(columnName) && !"gid".equals(columnName)) {
                    searchBlock.append("                    ").append(lowerColumnName).append(": null,\n");
                }
            }
            if (dictionaryColumnList.contains(columnName)) {
                dictionarySearchBlock.append("                    ").append(lowerColumnName).append("List: [],\n");
                dictionaryMapBlock.append("                ").append(lowerColumnName)
                        .append("Key: \"").append(tableName).append("_").append(columnName).append("\",\n                ")
                        .append(lowerColumnName).append("Map: this.getDictionaryMap(\"").append(tableName).append("_").append(columnName).append("\"),\n");
                dictionaryFilterBlock.append("                this.").append(lowerTableName)
                        .append("VO.").append(lowerColumnName).append("List = data.").append(lowerColumnName).append(";\n");
            }
            if (addCheck.contains(columnName) || updateCheck.contains(columnName) || detailCheck.contains(columnName)) {
                beanBlock.append("                    ").append(lowerColumnName).append(": null,\n");
            }
            if (sortCheck.contains(columnName)) {
                sortBlock.append("                    case \"").append(lowerColumnName)
                        .append("\":\n                        this.").append(lowerTableName)
                        .append("VO.sortLabel = \"").append(columnName).append("\";\n                        break;\n                }\n");
            }
            if (!column.getIsNullAble() && addCheck.contains(columnName) && updateCheck.contains(columnName)) {
                rulesBlock.append("                    ").append(lowerColumnName).append(": [{required: true, message: '").append(column.getColumnComment()).append("不能为空'}],\n");
            }
        }
        if (CollectionUtils.isNotEmpty(sortCheck)) {
            sortBlock.append("                this.").append(lowerTableName)
                    .append("VO.sortOrder = data.order ? data.order.replace(\"ending\", \"\") : null;\n                this.handleSearch();\n            },\n");
        }
        if (hasUpdate) {
            handleUpdateBlock.append("                this.updateVisible = true;\n            },\n");
        }
        if (hasAdd) {
            addShowBlock.append("                this.addVisible = true;\n            },\n");
        }
        return "\n<script>\n    export default {\n        data() {\n            return {\n" +
                addVisible + updateVisible + detailVisible + timeRangeBlock.toString() + timePickerBlock.toString() +
                "                " + lowerTableName +
                "VO: {\n                    page: 1,\n                    size: 10,\n" + sortParam + deleteParam +
                timeSearchBlock.toString() + dictionarySearchBlock.toString() + searchBlock.toString() +
                "                },\n                pageBean: {\n                    page: 1,\n                    total: 0,\n                    data: [],\n                },\n                " +
                lowerTableName + ": {\n" + beanBlock.toString() + "                },\n" + dictionaryMapBlock.toString() + "                rules: {\n" + rulesBlock.toString() +
                "                },\n            }\n        },\n        methods: {\n            handleSizeChange(val) {\n                this." + lowerTableName +
                "VO.size = val;\n                this.handleSearch();\n            },\n            handlePageChange(val) {\n                this." + lowerTableName +
                "VO.page = val;\n                this.handleSearch();\n            },\n            handleSearch() {\n                this.$axios.post(\"/" + prefix + "List\", this." + lowerTableName +
                "VO).then(response => {\n                    let data = response.data;\n                    if (data.status === 0) {\n                        this.pageBean = data.data;\n                    }\n                });\n            },\n" +
                handleTimeRangeBlock.toString() + sortBlock.toString() + "            handleFilterChange(data) {\n" + dictionaryFilterBlock.toString() + "                this.handleSearch();\n            },\n" +
                handlePropertyBlock.toString() + handleUpdateBlock.toString() + handleDetailBlock.toString() + addShowBlock.toString() +
                "            handleClear() {\n                for (let key in this." + lowerTableName + ") {\n                    if (this." + lowerTableName +
                ".hasOwnProperty(key)) {\n                        this." + lowerTableName + "[key] = null;\n                    }\n                }\n            },\n" +
                handleAddUpdateBlock.toString() + handleDeleteBlock.toString() +
                "        mounted() {\n            this.handleSearch();\n        },\n       computed: {\n            fullHeight() {\n                return this.$store.state.fullHeight - 140;\n            }\n        }\n    }\n</script>";
    }
}
