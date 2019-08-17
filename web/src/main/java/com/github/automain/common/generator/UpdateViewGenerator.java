package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateViewGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String executorPrefix, String[] dictionaryCheck, String[] updateCheck) {
        if (databaseName == null || tableName == null || updateCheck == null) {
            return null;
        }
        try {
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            ColumnBean priColumn = getPrimaryColumn(columns);
            String resultStr = "";

            resultStr += getJspHead();
            List<String> dictionaryList = null;
            if (dictionaryCheck != null) {
                dictionaryList = Arrays.asList(dictionaryCheck);
            } else {
                dictionaryList = new ArrayList<String>();
            }
            List<String> updateCheckList = Arrays.asList(updateCheck);

            resultStr += getContentArea(columns, priColumn, executorPrefix, updateCheckList, dictionaryList);

            resultStr += getScript(convertToJavaName(tableName, false), columns, executorPrefix, updateCheckList, dictionaryList);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getContentArea(List<ColumnBean> columns, ColumnBean priColumn, String executorPrefix, List<String> updateCheck, List<String> dictionaryList) {
        String javaPriColumnName = convertToJavaName(priColumn.getColumnName(), false);
        StringBuilder returnStr = new StringBuilder();
        returnStr.append("<form class=\"layui-form layui-form-pane\" action=\"\">\n")
                .append("    <input type=\"hidden\" name=\"")
                .append(javaPriColumnName).append("\" value=\"${bean.")
                .append(javaPriColumnName).append("}\">\n");
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String javaColumnName = convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            if (updateCheck.contains(columnName)) {
                if (dictionaryList.contains(columnName)) {
                    returnStr.append("    <div class=\"layui-form-item\">\n")
                            .append("        <label class=\"layui-form-label\">")
                            .append(columnComment).append("</label>\n")
                            .append("        <div class=\"layui-input-block\">\n")
                            .append("            <select name=\"").append(javaColumnName)
                            .append("\">\n").append("                <c:forEach items=\"${")
                            .append(javaColumnName).append("VOList}\" var=\"item\">\n")
                            .append("                    <option value=\"${item.dictionaryValue}\" <c:if test=\"${bean.")
                            .append(javaColumnName)
                            .append(" == item.dictionaryValue}\">selected=\"selected\"</c:if>>${item.dictionaryName}</option>\n")
                            .append("                </c:forEach>\n")
                            .append("            </select>\n")
                            .append("        </div>\n").append("    </div>\n");
                } else {
                    String dataType = column.getDataType();
                    switch (dataType) {
                        case "String":
                            if (column.getIsTextArea()) {
                                returnStr.append("    <div class=\"layui-form-item layui-form-text\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <textarea class=\"layui-textarea\" name=\"")
                                        .append(javaColumnName).append("\" lay-verify=\"")
                                        .append(columnName).append("\"><c:out value=\"${bean.")
                                        .append(javaColumnName)
                                        .append("}\"/></textarea>\n").append("        </div>\n")
                                        .append("    </div>\n");
                            } else {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"text\" class=\"layui-input\" autocomplete=\"off\" name=\"")
                                        .append(javaColumnName).append("\" lay-verify=\"")
                                        .append(columnName).append("\" value=\"<c:out value='${bean.")
                                        .append(javaColumnName)
                                        .append("}'/>\">\n").append("        </div>\n")
                                        .append("    </div>\n");
                            }
                            break;
                        case "Date":
                        case "Timestamp":
                        case "Time":
                            returnStr.append("    <div class=\"layui-form-item\">\n")
                                    .append("        <label class=\"layui-form-label\">")
                                    .append(columnComment).append("</label>\n")
                                    .append("        <div class=\"layui-input-block\">\n")
                                    .append("            <input type=\"text\" class=\"layui-input\" id=\"")
                                    .append(javaColumnName).append("\" name=\"")
                                    .append(javaColumnName).append("\" lay-verify=\"").append(columnName)
                                    .append("\"  value=\"<fmt:formatDate value='${bean.")
                                    .append(javaColumnName)
                                    .append("}' pattern='yyyy-MM-dd HH:mm:ss'/>\">\n")
                                    .append("        </div>\n").append("    </div>\n");
                            break;
                        case "Long":
                        case "BigDecimal":
                        case "Double":
                        case "Float":
                            returnStr.append("    <div class=\"layui-form-item\">\n")
                                    .append("        <label class=\"layui-form-label\">")
                                    .append(columnComment).append("</label>\n")
                                    .append("        <div class=\"layui-input-block\">\n")
                                    .append("            <input type=\"number\" class=\"layui-input\" autocomplete=\"off\" name=\"")
                                    .append(javaColumnName).append("\" lay-verify=\"")
                                    .append(columnName).append("\" value=\"${bean.")
                                    .append(javaColumnName)
                                    .append("}\">\n").append("        </div>\n").append("    </div>\n");
                            break;
                        case "Integer":
                            if (checkTimeTypeColumn(column)) {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"text\" class=\"layui-input\" id=\"")
                                        .append(javaColumnName).append("\" name=\"")
                                        .append(javaColumnName).append("\" lay-verify=\"").append(columnName)
                                        .append("\"  value=\"<fmt:formatDate value='${bean.")
                                        .append(javaColumnName)
                                        .append("}' pattern='yyyy-MM-dd HH:mm:ss'/>\">\n")
                                        .append("        </div>\n").append("    </div>\n");
                            } else {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"number\" class=\"layui-input\" autocomplete=\"off\" name=\"")
                                        .append(javaColumnName).append("\" lay-verify=\"")
                                        .append(columnName).append("\" value=\"${bean.")
                                        .append(javaColumnName)
                                        .append("}\">\n").append("        </div>\n").append("    </div>\n");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        returnStr.append("    <div class=\"layui-form-item\">\n")
                .append("        <div class=\"layui-input-block\">\n")
                .append("            <button class=\"layui-btn\" lay-submit lay-filter=\"")
                .append(executorPrefix).append("_submit\">立即提交</button>\n")
                .append("        </div>\n").append("    </div>\n")
                .append("</form>\n").append("</body>\n").append("</html>\n");
        return returnStr.toString();
    }

    private String getScript(String lowerTableName, List<ColumnBean> columns, String executorPrefix, List<String> updateCheckList, List<String> dictionaryList) {
        StringBuilder returnStr = new StringBuilder("<script>\n" +
                "    layui.use(['form', 'layer', 'laydate'], function () {\n" +
                "        var form = layui.form\n" +
                "            , layer = layui.layer\n" +
                "            , laydate = layui.laydate;\n" +
                "        form.verify({\n");
        int i = 0;
        StringBuilder dateInit = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String lowerColumnName = convertToJavaName(columnName, false);
            if (updateCheckList.contains(columnName) && !dictionaryList.contains(columnName)) {
                if (i > 0) {
                    returnStr.append("            , ");
                } else {
                    returnStr.append("            ");
                }
                returnStr.append(columnName).append(": function (value) {\n")
                        .append("                if (value.length == 0) {\n")
                        .append("                    return '请输入").append(column.getColumnComment()).append("';\n")
                        .append("                }\n")
                        .append("            }\n");
                i++;
                if (checkTimeTypeColumn(column)) {
                    dateInit.append("\n        laydate.render({\n")
                            .append("            elem: '#").append(lowerColumnName).append("'\n")
                            .append("            , type: 'datetime'\n")
                            .append("            , value: '<fmt:formatDate value=\"${").append("bean.")
                            .append(lowerColumnName).append("}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/>'\n")
                            .append("        });");
                }
            }
        }
        returnStr.append("        });").append(dateInit)
                .append("\n        form.on('submit(").append(executorPrefix)
                .append("_submit)', function (data) {\n")
                .append("            var submitBtn = $(this);\n")
                .append("            if (!submitBtn.hasClass(\"layui-btn-disabled\")) {\n")
                .append("                submitBtn.addClass(\"layui-btn-disabled\");\n")
                .append("                var index = parent.layer.getFrameIndex(window.name);\n")
                .append("                $.post(\"${ctx}/").append(executorPrefix.replace("_", "/"))
                .append("/update\", data.field, function (data) {\n")
                .append("                    layer.msg(data.msg);\n")
                .append("                    if (data.code == code_success) {\n")
                .append("                        parent.layer.close(index);\n")
                .append("                        parent.reload")
                .append(convertToJavaName(executorPrefix, true)).append("List(1);\n")
                .append("                    } else {\n")
                .append("                        submitBtn.removeClass(\"layui-btn-disabled\");\n")
                .append("                    }\n")
                .append("                }, \"json\");\n")
                .append("            }\n")
                .append("            return false;\n")
                .append("        });\n")
                .append("    });\n")
                .append("</script>");
        return returnStr.toString();
    }
}
