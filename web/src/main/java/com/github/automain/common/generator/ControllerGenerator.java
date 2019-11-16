package com.github.automain.common.generator;

public class ControllerGenerator {

    public String generate(String prefix, String upperPrefix, String tableName, String upperTableName, boolean hasList, boolean hasAdd, boolean hasUpdate, boolean hasDetail, boolean hasIsValid, boolean hasGlobalId, boolean hasCreateTime, boolean hasUpdateTime) {
        String daoName = tableName.toUpperCase() + "_DAO";
        String resultStr = "";

        resultStr += getImportHead(hasIsValid, hasCreateTime, hasUpdateTime, hasGlobalId, upperTableName);

        resultStr += getClassHead(upperPrefix);

        if (hasList) {
            resultStr += getList(prefix, upperTableName, daoName);
        }

        if (hasAdd) {
            resultStr += getAdd(prefix, upperTableName, hasCreateTime, hasUpdateTime, hasGlobalId, daoName);
        }

        if (hasAdd || hasUpdate) {
            resultStr += getCheckValid(upperTableName, hasGlobalId);
        }

        if (hasUpdate) {
            resultStr += getUpdate(prefix, upperTableName, hasUpdateTime, hasGlobalId, daoName);
        }

        if (hasDetail || hasUpdate) {
            resultStr += getDetail(prefix, upperTableName, hasGlobalId, daoName);
        }

        if (hasIsValid) {
            resultStr += getDelete(prefix, upperTableName, hasGlobalId, daoName);
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
        return "public class " + upperPrefix + "Controller implements ServiceDaoContainer {";
    }

    private String getList(String prefix, String upperTableName, String daoName) {
        return "\n\n    @RequestUri(value = \"/" + prefix + "List\", slave = \"slave1\")\n    public JsonResponse " + prefix +
                "List(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + "VO vo = getRequestParam(request, " + upperTableName +
                "VO.class);\n        if (vo != null) {\n            PageBean<" + upperTableName + "> pageBean = " + daoName +
                ".selectTableForCustomPage(connection, vo);\n            return JsonResponse.getSuccessJson(pageBean);\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getAdd(String prefix, String upperTableName, boolean hasCreateTime, boolean hasUpdateTime, boolean hasGlobalId, String daoName) {
        String updateTimeSet = hasUpdateTime ? "            bean.setUpdateTime(DateUtil.getNow());\n" : "";
        String createTimeSet = hasCreateTime ? hasUpdateTime
                ? "            bean.setCreateTime(bean.getUpdateTime());\n"
                : "            bean.setCreateTime(DateUtil.getNow());\n" : "";
        String gidSet = hasGlobalId ? "            bean.setGid(UUID.randomUUID().toString());\n" : "";
        return "\n\n    @RequestUri(\"/" + prefix + "Add\")\n    public JsonResponse " + prefix +
                "Add(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n        if (checkValid(bean, false)) {\n" +
                updateTimeSet + createTimeSet + gidSet + "            " + daoName +
                ".insertIntoTable(connection, bean);\n            return JsonResponse.getSuccessJson();\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getCheckValid(String upperTableName, boolean hasGlobalId) {
        String idCheck = hasGlobalId ? "bean.getGid()" : "bean.getId()";
        return "\n\n    private boolean checkValid(" + upperTableName
                + " bean, boolean isUpdate) {\n        return bean != null && (!isUpdate || " + idCheck + " != null);\n    }";
    }

    private String getUpdate(String prefix, String upperTableName, boolean hasUpdateTime, boolean hasGlobalId, String daoName) {
        String updateTimeSet = hasUpdateTime ? "            bean.setUpdateTime(DateUtil.getNow());\n" : "";
        String updateFun = hasGlobalId ? "            " + daoName + ".updateTableByGid(connection, bean, false);\n"
                : "            " + daoName + ".updateTableById(connection, bean, false);\n";
        return "\n\n    @RequestUri(\"/" + prefix + "Update\")\n    public JsonResponse " + prefix +
                "Update(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n        if (checkValid(bean, true)) {\n" +
                updateTimeSet + updateFun +
                "            return JsonResponse.getSuccessJson();\n        }\n        return JsonResponse.getFailedJson();\n    }";
    }

    private String getDetail(String prefix, String upperTableName, boolean hasGlobalId, String daoName) {
        String detailContent = hasGlobalId
                ? "        if (bean != null && bean.getGid() != null) {\n            " + upperTableName + " detail = " + daoName +
                ".selectTableByGid(connection, bean);\n            return JsonResponse.getSuccessJson(detail);\n        }\n"
                : "        if (bean != null && bean.getId() != null) {\n            " + upperTableName + " detail = " + daoName +
                ".selectTableById(connection, bean);\n            return JsonResponse.getSuccessJson(detail);\n        }\n";
        return "\n\n    @RequestUri(value = \"/" + prefix + "Detail\", slave = \"slave1\")\n    public JsonResponse " + prefix +
                "Detail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n" +
                detailContent + "        return JsonResponse.getFailedJson();\n    }";
    }

    private String getDelete(String prefix, String upperTableName, boolean hasGlobalId, String daoName) {
        String deleteContent = hasGlobalId
                ? "        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {\n            " + daoName +
                ".softDeleteTableByGidList(connection, vo.getGidList());\n            return JsonResponse.getSuccessJson();\n        }\n"
                : "        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {\n            " + daoName +
                ".softDeleteTableByIdList(connection, vo.getIdList());\n            return JsonResponse.getSuccessJson();\n        }\n";
        return "\n\n    @RequestUri(\"/" + prefix + "Delete\")\n    public JsonResponse " + prefix +
                "Delete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        " +
                upperTableName + "VO vo = getRequestParam(request, " + upperTableName + "VO.class);\n" +
                deleteContent + "        return JsonResponse.getFailedJson();\n    }";
    }
}
