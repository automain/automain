package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabViewGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String executorPrefix, String[] listCheck, boolean deleteCheck, boolean hasAdd, boolean hasUpdate, boolean hasDetail, String[] searchCheck, String[] dictionaryCheck) {
        if (databaseName == null || tableName == null || listCheck == null) {
            return null;
        }
        try {
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            ColumnBean priColumn = getPrimaryColumn(columns);
            String resultStr = "";

            resultStr += getJspHead();

            List<String> searchCheckList = new ArrayList<String>();
            if (searchCheck != null && searchCheck.length > 0) {
                searchCheckList = Arrays.asList(searchCheck);
            }

            List<String> dictionaryList = new ArrayList<String>();
            if (dictionaryCheck != null) {
                dictionaryList = Arrays.asList(dictionaryCheck);
            }

            resultStr += getControlArea(columns, executorPrefix, deleteCheck, searchCheckList, dictionaryList, hasAdd);

            resultStr += getContentArea(tableName, columns, priColumn, executorPrefix, Arrays.asList(listCheck), deleteCheck, hasUpdate, hasDetail);

            resultStr += getScript(tableName, columns, executorPrefix, priColumn, deleteCheck, hasAdd, hasUpdate, hasDetail, searchCheckList);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getControlArea(List<ColumnBean> columns, String executorPrefix, boolean deleteCheck, List<String> searchCheckList, List<String> dictionaryList, boolean hasAdd) {
        StringBuilder returnStr = new StringBuilder();
        returnStr.append("<div class=\"layui-fluid\">\n")
                .append("    <div class=\"layui-row\">\n")
                .append("        <div class=\"layui-col-md12\">\n")
                .append("            <div class=\"layui-card\">\n")
                .append("                <blockquote class=\"layui-elem-quote\">\n")
                .append("                    <div class=\"layui-form\">\n");
        if (hasAdd) {
            returnStr.append("                        <div class=\"layui-inline\">\n")
                    .append("                            <button class=\"layui-btn layui-btn-sm layui-btn-normal\" id=\"")
                    .append(executorPrefix).append("_add\">\n")
                    .append("                                <i class=\"fa fa-plus\"></i> 添加\n")
                    .append("                            </button>\n")
                    .append("                        </div>\n");
        }
        if (deleteCheck) {
            returnStr.append("                        <div class=\"layui-inline\">\n")
                    .append("                            <button class=\"layui-btn layui-btn-sm layui-btn-danger\" id=\"")
                    .append(executorPrefix).append("_delete\">\n")
                    .append("                                <i class=\"fa fa-remove\"></i> 删除\n")
                    .append("                            </button>\n")
                    .append("                        </div>\n");
        }
        boolean hasSearch = false;
        if (!searchCheckList.isEmpty()) {
            hasSearch = true;
            for (ColumnBean column : columns) {
                String columnName = column.getColumnName();
                String javaColumnName = convertToJavaName(columnName, false);
                String columnComment = column.getColumnComment();
                if (searchCheckList.contains(columnName)) {
                    if (dictionaryList.contains(columnName)) {
                        returnStr.append("                        <div class=\"layui-inline\">\n")
                                .append("                            <select id=\"").append(columnName).append("_search").append("\">\n")
                                .append("                                <option value=\"\">请选择").append(columnComment).append("</option>\n")
                                .append("                                <c:forEach items=\"${")
                                .append(javaColumnName).append("VOList}\" var=\"item\">\n")
                                .append("                                    <option value=\"${item.dictionaryValue}\">${item.dictionaryName}</option>\n")
                                .append("                                </c:forEach>\n")
                                .append("                            </select>\n")
                                .append("                        </div>\n");
                    } else {
                        String dataType = column.getDataType();
                        switch (dataType) {
                            case "String":
                            case "Date":
                            case "Timestamp":
                            case "Time":
                                returnStr.append("                        <div class=\"layui-inline\">\n")
                                        .append("                            <input type=\"text\" class=\"layui-input\" autocomplete=\"off\" id=\"")
                                        .append(columnName).append("_search").append("\" placeholder=\"请输入").append(columnComment).append("\">\n")
                                        .append("                        </div>\n");
                                break;
                            case "Long":
                            case "BigDecimal":
                            case "Double":
                            case "Float":
                            case "Integer":
                                returnStr.append("                        <div class=\"layui-inline\">\n")
                                        .append("                            <input type=\"number\" class=\"layui-input\" autocomplete=\"off\" id=\"")
                                        .append(columnName).append("_search").append("\" placeholder=\"请输入").append(columnComment).append("\">\n")
                                        .append("                        </div>\n");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        returnStr.append("                        <div class=\"layui-inline\">\n")
                .append("                            <button class=\"layui-btn layui-btn-sm layui-btn-warm\" id=\"")
                .append(executorPrefix);
        if (hasSearch) {
            returnStr.append("_search\">\n").append("                                <i class=\"fa fa-search\"></i> 搜索\n");
        } else {
            returnStr.append("_refresh\">\n").append("                                <i class=\"fa fa-refresh\"></i> 刷新\n");
        }
        returnStr.append("                            </button>\n")
                .append("                        </div>\n")
                .append("                    </div>\n")
                .append("                </blockquote>\n");
        return returnStr.toString();
    }

    private String getContentArea(String tableName, List<ColumnBean> columns, ColumnBean priColumn, String executorPrefix, List<String> listCheck, boolean deleteCheck, boolean hasUpdate, boolean hasDetail) {
        StringBuilder returnStr = new StringBuilder();
        returnStr.append("                <table class=\"layui-table\" lay-skin=\"line\" lay-filter=\"").append(tableName).append("\" lay-data=\"{id: '").append(tableName).append("'}\">\n")
                .append("                    <thead>\n")
                .append("                    <tr>\n");
        if (deleteCheck) {
            returnStr.append("                        <th lay-data=\"{field:'").append(priColumn.getColumnName()).append("',checkbox:true, fixed:'left'}\"></th>\n");
        }
        for (ColumnBean column : columns) {
            if (listCheck.contains(column.getColumnName())) {
                returnStr.append("                        <th lay-data=\"{field:'").append(column.getColumnName()).append("', width:160}\">").append(column.getColumnComment()).append("</th>\n");
            }
        }
        if (hasUpdate || hasDetail) {
            int width = 0;
            width += hasUpdate ? 90 : 0;
            width += hasDetail ? 90 : 0;
            returnStr.append("                        <th lay-data=\"{field:'operation', width:").append(width).append(", fixed:'right'}\">操作</th>\n");
        }
        returnStr.append("                    </tr>\n")
                .append("                    </thead>\n")
                .append("                    <tbody id=\"").append(executorPrefix).append("_list_body\">\n")
                .append("                    </tbody>\n")
                .append("                </table>\n")
                .append("                <div id=\"").append(executorPrefix).append("_page\"></div>\n")
                .append("            </div>\n")
                .append("        </div>\n")
                .append("    </div>\n")
                .append("</div>\n")
                .append("</body>\n")
                .append("</html>\n");
        return returnStr.toString();
    }

    private String getScript(String tableName, List<ColumnBean> columns, String executorPrefix, ColumnBean priColumn, boolean deleteCheck, boolean hasAdd, boolean hasUpdate, boolean hasDetail, List<String> searchCheckList) {
        String upperPrefix = convertToJavaName(executorPrefix, true);
        String lowerPriColumnName = convertToJavaName(priColumn.getColumnName(), false);
        String urlPerfix = executorPrefix.replace("_", "/");
        StringBuilder returnStr = new StringBuilder();
        String dateImport = "";
        if (!searchCheckList.isEmpty()) {
            for (ColumnBean column : columns) {
                if (searchCheckList.contains(column.getColumnName())) {
                    String dataType = column.getDataType();
                    if ("Timestamp".equals(dataType) || "Date".equals(dataType) || "Time".equals(dataType)) {
                        dateImport = ", 'laydate'";
                        break;
                    }
                }
            }
        }
        returnStr.append("<script>\n").append("    var form, laypage, layer, table;\n")
                .append("    layui.use(['form', 'layer', 'laypage', 'table'").append(dateImport).append("], function () {\n")
                .append("        form = layui.form;\n")
                .append("        layer = layui.layer;\n")
                .append("        laypage = layui.laypage;\n")
                .append("        table = layui.table;\n");
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        if (!searchCheckList.isEmpty()) {
            for (ColumnBean column : columns) {
                String columnName = column.getColumnName();
                if (searchCheckList.contains(columnName)) {
                    String upperColumnName = convertToJavaName(columnName, false);
                    switch (column.getDataType()) {
                        case "Date":
                        case "Timestamp":
                        case "Time":
                            returnStr.append("        var laydate = layui.laydate;\n")
                                    .append("        laydate.render({\n")
                                    .append("            elem: '#").append(columnName).append("_search'\n")
                                    .append("            ,type: 'datetime'\n")
                                    .append("            ,range: true\n")
                                    .append("        });\n");
                            paramMap.put(upperColumnName + "Range", columnName);
                            break;
                        default:
                            paramMap.put(upperColumnName, columnName);
                            break;
                    }
                }
            }
        }
        if (hasAdd) {
            returnStr.append("        $(\"#").append(executorPrefix)
                    .append("_add\").click(function () {\n")
                    .append("            alertByFull(layer, \"添加\", \"${ctx}/")
                    .append(urlPerfix).append("/forward?forwardType=add\");\n")
                    .append("        });\n");
        }
        if (deleteCheck) {
            returnStr.append("        $(\"#").append(executorPrefix).append("_delete\").click(function () {\n")
                    .append("            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {\n")
                    .append("                var checkStatusData = table.checkStatus('").append(tableName).append("').data;\n")
                    .append("                var deleteCheck = new Array();\n")
                    .append("                checkStatusData.forEach(function(val, index){\n")
                    .append("                    deleteCheck.push(val.").append(priColumn.getColumnName()).append(");\n")
                    .append("                });\n")
                    .append("                doDelete(layer, deleteCheck, \"${ctx}/").append(urlPerfix).append("/delete\", reload")
                    .append(upperPrefix).append("List(1));\n").append("                layer.close(index);\n")
                    .append("            });\n").append("        });\n");
        }
        String pageBtn = "_refresh";
        if (!searchCheckList.isEmpty()) {
            pageBtn = "_search";
        }
        returnStr.append("        $(\"#").append(executorPrefix).append(pageBtn).append("\").click(function () {\n")
                .append("            reload").append(upperPrefix).append("List(1);\n")
                .append("        });\n").append("        reload").append(upperPrefix)
                .append("List(1);\n").append("    });\n").append("    function reload")
                .append(upperPrefix).append("List(page) {\n")
                .append("        var index = layer.load();\n")
                .append("        setTimeout(function () {\n")
                .append("            $.post(\"${ctx}/").append(urlPerfix)
                .append("/list\", {\n")
                .append("                page: page\n");
        if (!paramMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                returnStr.append("                ,").append(entry.getKey()).append(": ").append("$(\"#").append(entry.getValue()).append("_search\").val()\n");
            }
        }
        returnStr.append("            }, function (data) {\n")
                .append("                if (data.code == code_success) {\n")
                .append("                    $(\"#").append(executorPrefix).append("_list_body\").html(data.data);\n")
                .append("                    renderPage(laypage, \"").append(executorPrefix)
                .append("_page\", data.count, data.curr, reload").append(upperPrefix).append("List);\n")
                .append("                    table.init('").append(tableName).append("', {\n")
                .append("                        height: 'full-190'\n")
                .append("                    });\n");
        if (hasUpdate) {
            returnStr.append("                    $(\".update-btn\").click(function () {\n")
                    .append("                        var updateId = $(this).attr(\"update-id\");\n")
                    .append("                        alertByFull(layer, \"编辑\", \"${ctx}/")
                    .append(urlPerfix).append("/forward?forwardType=update&")
                    .append(lowerPriColumnName).append("=\" + updateId);\n")
                    .append("                    });\n");
        }
        if (hasDetail) {
            returnStr.append("                    $(\".detail-btn\").click(function () {\n")
                    .append("                        var detailId = $(this).attr(\"detail-id\");\n")
                    .append("                        alertByFull(layer, \"详情\", \"${ctx}/")
                    .append(urlPerfix).append("/forward?forwardType=detail&").append(lowerPriColumnName)
                    .append("=\" + detailId);\n").append("                    });\n");

        }
        returnStr.append("                }\n").append("                layer.close(index);\n")
                .append("            }, \"json\");\n").append("        }, loadingTime);\n")
                .append("    }\n").append("</script>");
        return returnStr.toString();
    }
}
