package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListViewGenerator extends CommonGenerator {

    public String generate(String databaseName, String tableName, String[] listCheck, String[] dictionaryCheck, boolean deleteCheck, boolean hasUpdate, boolean hasDetail) {
        if (databaseName == null || tableName == null || listCheck == null) {
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

            resultStr += getBody(columns, priColumn, Arrays.asList(listCheck), dictionaryList, deleteCheck, hasUpdate, hasDetail);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getBody(List<ColumnBean> columns, ColumnBean priColumn, List<String> listCheck, List<String> dictionaryCheck, boolean deleteCheck, boolean hasUpdate, boolean hasDetail) {
        String priColumnName = convertToJavaName(priColumn.getColumnName(), false);
        StringBuilder body = new StringBuilder();
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            String columnType = column.getDataType();
            String javaColumnName = convertToJavaName(columnName, false);
            if (listCheck.contains(columnName)) {
                if (dictionaryCheck.contains(columnName)) {
                    body.append("            <td>${").append(javaColumnName)
                            .append("Map[fn:trim(item.").append(javaColumnName)
                            .append(")]}</td>\n");
                } else {
                    switch (columnType) {
                        case "Date":
                        case "Timestamp":
                            body.append("            <td><fmt:formatDate value=\"${item.")
                                    .append(javaColumnName)
                                    .append("}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>\n");
                            break;
                        case "Time":
                            body.append("            <td><fmt:formatDate value=\"${item.")
                                    .append(javaColumnName)
                                    .append("}\" pattern=\"HH:mm:ss\"/></td>\n");
                            break;
                        case "BigDecimal":
                        case "Double":
                        case "Float":
                            body.append("            <td><fmt:formatNumber type=\"number\" value=\"${item.")
                                    .append(javaColumnName)
                                    .append("}\" pattern=\"0.00\" maxFractionDigits=\"2\"/></td>\n");
                            break;
                        case "Integer":
                            if (checkTimeTypeColumn(column)) {
                                body.append("            <td><fmt:formatDate value=\"${item.")
                                        .append(javaColumnName)
                                        .append("}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>\n");
                            } else {
                                body.append("            <td><c:out value=\"${item.")
                                        .append(javaColumnName).append("}\"/></td>\n");
                            }
                            break;
                        default:
                            body.append("            <td><c:out value=\"${item.")
                                    .append(javaColumnName).append("}\"/></td>\n");
                            break;
                    }
                }
            }
        }
        StringBuilder returnStr = new StringBuilder();
        returnStr.append("<table>\n").append("    <c:forEach items=\"${pageBean.data}\" var=\"item\">\n")
                .append("        <tr>\n");
        if (deleteCheck) {
            returnStr.append("            <td>").append("${item.").append(priColumnName).append("}</td>\n");
        }
        returnStr.append(body);
        if (hasUpdate || hasDetail) {
            returnStr.append("            <td>\n");
            if (hasUpdate) {
                returnStr.append("                <button class=\"layui-btn layui-btn-xs update-btn\" update-id=\"${item.")
                        .append(priColumnName)
                        .append("}\"><i class=\"fa fa-edit\"></i>编辑</button>\n");
            }
            if (hasDetail) {
                returnStr.append("                <button class=\"layui-btn layui-btn-xs detail-btn\" detail-id=\"${item.")
                        .append(priColumnName).append("}\"><i class=\"fa fa-list\"></i>详情</button>\n");
            }
            returnStr.append("            </td>\n");
        }
        returnStr.append("        </tr>\n").append("    </c:forEach>\n")
                .append("</table>\n").append("</body>\n").append("</html>");
        return returnStr.toString();
    }

}
