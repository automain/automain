package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailViewGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String[] dictionaryCheck, String[] detailCheck) {
        if (databaseName == null || tableName == null || detailCheck == null) {
            return null;
        }
        try {
            List<ColumnBean> columns = selectAllColumnList(null, databaseName, tableName);
            String resultStr = "";

            resultStr += getJspHead();
            List<String> dictionaryList = null;
            if (dictionaryCheck != null) {
                dictionaryList = Arrays.asList(dictionaryCheck);
            } else {
                dictionaryList = new ArrayList<String>();
            }
            List<String> detailCheckList = Arrays.asList(detailCheck);

            resultStr += getContentArea(columns, detailCheckList, dictionaryList);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getContentArea(List<ColumnBean> columns, List<String> detailCheck, List<String> dictionaryList) {
        StringBuilder returnStr = new StringBuilder();
        returnStr.append("<form class=\"layui-form layui-form-pane\" >\n");
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String javaColumnName = convertToJavaName(columnName, false);
            String columnComment = column.getColumnComment();
            if (detailCheck.contains(columnName)) {
                if (dictionaryList.contains(columnName)) {
                    returnStr.append("    <div class=\"layui-form-item\">\n").append(
                            "        <label class=\"layui-form-label\">")
                            .append(columnComment).append("</label>\n")
                            .append("        <div class=\"layui-input-block\">\n")
                            .append("            <c:forEach items=\"${")
                            .append(javaColumnName).append("VOList}\" var=\"item\">\n")
                            .append("                <c:if test=\"${bean.").append(javaColumnName)
                            .append(" == item.dictionaryValue}\"><input type=\"text\" disabled=\"disabled\" class=\"layui-input\" value=\"${item.dictionaryName}\"/></c:if>\n")
                            .append("            </c:forEach>\n").append("        </div>\n").append("    </div>\n");
                } else {
                    String dataType = column.getDataType();
                    switch (dataType) {
                        case "String":
                            if (column.getIsTextArea()) {
                                returnStr.append("    <div class=\"layui-form-item layui-form-text\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <textarea class=\"layui-textarea\" disabled=\"disabled\"><c:out value=\"${bean.")
                                        .append(javaColumnName)
                                        .append("}\"/></textarea>\n").append("        </div>\n").append("    </div>\n");
                            } else {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"text\" disabled=\"disabled\" class=\"layui-input\" value=\"<c:out value='${bean.")
                                        .append(javaColumnName)
                                        .append("}'/>\">\n").append("        </div>\n").append("    </div>\n");
                            }
                            break;
                        case "Date":
                        case "Timestamp":
                        case "Time":
                            returnStr.append("    <div class=\"layui-form-item\">\n")
                                    .append("        <label class=\"layui-form-label\">")
                                    .append(columnComment).append("</label>\n")
                                    .append("        <div class=\"layui-input-block\">\n")
                                    .append("            <input type=\"text\" disabled=\"disabled\" class=\"layui-input\" value=\"<fmt:formatDate value='${bean.")
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
                                    .append("            <input type=\"number\" disabled=\"disabled\" class=\"layui-input\" value=\"${bean.")
                                    .append(javaColumnName)
                                    .append("}\">\n").append("        </div>\n").append("    </div>\n");
                            break;
                        case "Integer":
                            if (checkTimeTypeColumn(column)) {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"text\" disabled=\"disabled\" class=\"layui-input\" value=\"<fmt:formatDate value='${bean.")
                                        .append(javaColumnName)
                                        .append("}' pattern='yyyy-MM-dd HH:mm:ss'/>\">\n")
                                        .append("        </div>\n").append("    </div>\n");
                            } else {
                                returnStr.append("    <div class=\"layui-form-item\">\n")
                                        .append("        <label class=\"layui-form-label\">")
                                        .append(columnComment).append("</label>\n")
                                        .append("        <div class=\"layui-input-block\">\n")
                                        .append("            <input type=\"number\" disabled=\"disabled\" class=\"layui-input\" value=\"${bean.")
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
        returnStr.append("</form>\n</body>\n</html>\n");
        return returnStr.toString();
    }

}
