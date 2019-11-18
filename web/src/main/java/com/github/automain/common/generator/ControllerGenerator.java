package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;

import java.util.List;

public class ControllerGenerator {

    public String generate(List<ColumnBean> columns, String prefix, String upperPrefix, String tableName, String upperTableName, boolean hasList, boolean hasAdd, boolean hasUpdate, boolean hasDetail, boolean hasIsValid, boolean hasGlobalId, boolean hasCreateTime, boolean hasUpdateTime) {
        String resultStr = "";

        resultStr += getImportHead(hasIsValid, hasCreateTime, hasUpdateTime, hasGlobalId, upperTableName);

        resultStr += getClassHead(upperPrefix);

        if (hasList) {
            resultStr += getList(prefix, upperTableName);
        }

        if (hasAdd) {
            resultStr += getAdd(prefix, upperTableName, hasCreateTime, hasUpdateTime, hasGlobalId);
        }

        if (hasAdd || hasUpdate) {
            resultStr += getCheckValid(upperTableName, columns);
        }

        if (hasUpdate) {
            resultStr += getUpdate(prefix, upperTableName, hasUpdateTime, hasGlobalId);
        }

        if (hasDetail || hasUpdate) {
            resultStr += getDetail(prefix, upperTableName, hasGlobalId);
        }

        if (hasIsValid) {
            resultStr += getDelete(prefix, upperTableName, hasGlobalId);
        }

        resultStr += "\n}";
        return resultStr;
    }

    private String getImportHead(boolean hasIsValid, boolean hasCreateTime, boolean hasUpdateTime, boolean hasGlobalId, String upperTableName) {
        String dateImport = (hasCreateTime || hasUpdateTime) ? "import com.github.automain.util.DateUtil;\n" : "";
        String collectionUtilsImport = hasIsValid ? "import org.apache.commons.collections4.CollectionUtils;\n" : "";
        String uuidImport = hasGlobalId ? "import java.util.UUID;\n" : "";
        return "package com.github.automain.controller;\n\n" +
                "import com.github.automain.bean." + upperTableName + ";\n" +
                "import com.github.automain.common.annotation.RequestUri;\n" +
                "import com.github.automain.common.bean.JsonResponse;\n" +
                "import com.github.automain.common.controller.BaseController;\n" +
                "import com.github.automain.dao." + upperTableName + "Dao;\n" +
                dateImport +
                "import com.github.automain.vo." + upperTableName + "VO;\n" +
                "import com.github.fastjdbc.PageBean;\n" +
                collectionUtilsImport +
                "import redis.clients.jedis.Jedis;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n" +
                "import java.sql.Connection;\n" +
                uuidImport + "\n";
    }

    private String getClassHead(String upperPrefix) {
        return "public class " + upperPrefix + "Controller extends BaseController {";
    }

    private String getList(String prefix, String upperTableName) {
        return "\n\n    @RequestUri(value = \"/" + prefix + "List\", slave = \"slave1\")\n    public JsonResponse " + prefix +
                "List(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + "VO vo = getRequestParam(request, " + upperTableName +
                "VO.class);\n        if (vo != null) {\n            PageBean<" + upperTableName + "> pageBean = " + upperTableName +
                "Dao.selectTableForCustomPage(connection, vo);\n            return JsonResponse.getSuccessJson(pageBean);\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getAdd(String prefix, String upperTableName, boolean hasCreateTime, boolean hasUpdateTime, boolean hasGlobalId) {
        String updateTimeSet = hasUpdateTime ? "            bean.setUpdateTime(DateUtil.getNow());\n" : "";
        String createTimeSet = hasCreateTime ? hasUpdateTime
                ? "            bean.setCreateTime(bean.getUpdateTime());\n"
                : "            bean.setCreateTime(DateUtil.getNow());\n" : "";
        String gidSet = hasGlobalId ? "            bean.setGid(UUID.randomUUID().toString());\n" : "";
        return "\n\n    @RequestUri(\"/" + prefix + "Add\")\n    public JsonResponse " + prefix +
                "Add(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n        if (checkValid(bean)) {\n" +
                updateTimeSet + createTimeSet + gidSet + "            " + upperTableName +
                "Dao.insertIntoTable(connection, bean);\n            return JsonResponse.getSuccessJson();\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getCheckValid(String upperTableName, List<ColumnBean> columns) {
        StringBuilder nullCheck = new StringBuilder();
        List<String> notCheckList = List.of("id", "gid", "is_valid", "create_time", "update_time");
        for (ColumnBean column : columns) {
            String columnName = column.getColumnName();
            if (!column.getIsNullAble() && !notCheckList.contains(columnName)) {
                nullCheck.append(" && bean.get").append(CommonGenerator.convertToJavaName(columnName, true)).append("() != null");
            }
        }
        return "\n\n    private boolean checkValid(" + upperTableName + " bean) {\n        return bean != null" + nullCheck.toString() + ";\n    }";
    }

    private String getUpdate(String prefix, String upperTableName, boolean hasUpdateTime, boolean hasGlobalId) {
        String updateTimeSet = hasUpdateTime ? "            bean.setUpdateTime(DateUtil.getNow());\n" : "";
        String updateFun = hasGlobalId ? "            " + upperTableName + "Dao.updateTableByGid(connection, bean, false);\n"
                : "            " + upperTableName + "Dao.updateTableById(connection, bean, false);\n";
        String idCheck = hasGlobalId ? "bean.getGid() != null" : "bean.getId() != null";
        return "\n\n    @RequestUri(\"/" + prefix + "Update\")\n    public JsonResponse " + prefix +
                "Update(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n        if (checkValid(bean) && " + idCheck + ") {\n" +
                updateTimeSet + updateFun +
                "            return JsonResponse.getSuccessJson();\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getDetail(String prefix, String upperTableName, boolean hasGlobalId) {
        String detailContent = hasGlobalId
                ? "        if (bean != null && bean.getGid() != null) {\n            " + upperTableName + " detail = " + upperTableName +
                "Dao.selectTableByGid(connection, bean);\n            return JsonResponse.getSuccessJson(detail);\n        }\n"
                : "        if (bean != null && bean.getId() != null) {\n            " + upperTableName + " detail = " + upperTableName +
                "Dao.selectTableById(connection, bean);\n            return JsonResponse.getSuccessJson(detail);\n        }\n";
        return "\n\n    @RequestUri(value = \"/" + prefix + "Detail\", slave = \"slave1\")\n    public JsonResponse " + prefix +
                "Detail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n" +
                detailContent + "        return JsonResponse.getFailedJson();\n    }";
    }

    private String getDelete(String prefix, String upperTableName, boolean hasGlobalId) {
        String deleteContent = hasGlobalId
                ? "        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {\n            " + upperTableName +
                "Dao.softDeleteTableByGidList(connection, vo.getGidList());\n            return JsonResponse.getSuccessJson();\n        }\n"
                : "        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {\n            " + upperTableName +
                "Dao.softDeleteTableByIdList(connection, vo.getIdList());\n            return JsonResponse.getSuccessJson();\n        }\n";
        return "\n\n    @RequestUri(\"/" + prefix + "Delete\")\n    public JsonResponse " + prefix +
                "Delete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + "VO vo = getRequestParam(request, " + upperTableName + "VO.class);\n" +
                deleteContent + "        return JsonResponse.getFailedJson();\n    }";
    }
}
