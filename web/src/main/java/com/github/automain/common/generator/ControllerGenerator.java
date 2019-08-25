package com.github.automain.common.generator;

public class ControllerGenerator {

    public String generate(String prefix, String upperPrefix, String upperTableName, boolean hasList, boolean hasAdd, boolean hasUpdate, boolean hasDetail, boolean hasIsValid, boolean hasGlobalId, boolean hasCreateTime, boolean hasUpdateTime, String tableName) {
        try {
            String serviceName = tableName.toUpperCase() + "_SERVICE";
            String resultStr = "";

            resultStr += getImportHead(hasIsValid, hasCreateTime, hasUpdateTime);

            resultStr += getClassHead(upperPrefix);

            if (hasList) {
                resultStr += getList(prefix, upperTableName, serviceName);
            }

            if (hasAdd || hasUpdate) {
                resultStr += getInsertOrUpdate(prefix, upperTableName, hasCreateTime, hasUpdateTime, hasGlobalId, serviceName);
            }

            if (hasDetail) {
                resultStr += getDetail(prefix, upperTableName, hasGlobalId, serviceName);
            }

            if (hasIsValid) {
                resultStr += getDelete(prefix, upperTableName, hasGlobalId, serviceName);
            }

            resultStr += "\n}";
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImportHead(boolean hasIsValid, boolean hasCreateTime, boolean hasUpdateTime) {
        String dateImport = (hasCreateTime || hasUpdateTime) ? "import com.github.automain.util.DateUtil;\n" : "";
        String collectionUtilsImport = hasIsValid ? "import org.apache.commons.collections4.CollectionUtils;\n" : "";
        return "import com.github.automain.common.annotation.RequestUri;\n" +
                "import com.github.automain.common.bean.JsonResponse;\n" +
                "import com.github.automain.common.controller.BaseController;\n" +
                dateImport +
                "import com.github.fastjdbc.bean.ConnectionBean;\n" +
                "import com.github.fastjdbc.bean.PageBean;\n" +
                collectionUtilsImport +
                "import redis.clients.jedis.Jedis;\n\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n\n";
    }

    private String getClassHead(String upperPrefix) {
        return "public class " + upperPrefix + "Controller extends BaseController {";
    }

    private String getList(String prefix, String upperTableName, String serviceName) {
        return "\n\n    @RequestUri(\"/" + prefix + "/list\")\n    public JsonResponse " + prefix
                + "List(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        "
                + upperTableName + "VO vo = getRequestParam(request, " + upperTableName
                + "VO.class);\n        if (vo != null) {\n            PageBean<" + upperTableName + "> pageBean = " + serviceName
                + ".selectTableForCustomPage(connection, vo);\n            return JsonResponse.getSuccessJson(\"success\", pageBean);\n        }\n        return JsonResponse.getFailedJson(\"failed\");\n    }";
    }

    private String getInsertOrUpdate(String prefix, String upperTableName, boolean hasCreateTime, boolean hasUpdateTime, boolean hasGlobalId, String serviceName) {
        String updateTimeSet = hasUpdateTime ? "            bean.setUpdateTime(DateUtil.getNow());\n" : "";
        String createTimeSet = hasCreateTime ? hasUpdateTime
                ? "                bean.setCreateTime(bean.getUpdateTime());\n"
                : "                bean.setCreateTime(DateUtil.getNow());\n" : "";
        String insertUpdateContent = hasGlobalId
                ? "            if (bean.getGid() != null) {\n                " + serviceName
                + ".updateTableByGid(connection, bean, false);\n            } else {\n" + createTimeSet +
                "                " + serviceName + ".insertIntoTable(connection, bean);\n            }\n"
                : "            if (bean.getId() != null) {\n                " + serviceName
                + ".updateTableById(connection, bean, false);\n            } else {\n" + createTimeSet +
                "                " + serviceName + ".insertIntoTable(connection, bean);\n            }\n";
        return "\n\n    @RequestUri(\"/" + prefix + "/insertOrUpdate\")\n    public JsonResponse " + prefix
                + "InsertOrUpdate(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        "
                + upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n        if (bean != null) {\n"
                + updateTimeSet + insertUpdateContent +
                "            return JsonResponse.getSuccessJson(\"success\");\n        }\n        return JsonResponse.getFailedJson(\"failed\");\n    }";
    }

    private String getDetail(String prefix, String upperTableName, boolean hasGlobalId, String serviceName) {
        String detailContent = hasGlobalId
                ? "        if (bean != null && bean.getGid() != null) {\n            " + upperTableName + " detail = " + serviceName
                + ".selectTableByGid(connection, bean);\n            return JsonResponse.getSuccessJson(\"success\", detail);\n        }\n"
                : "        if (bean != null && bean.getId() != null) {\n            " + upperTableName + " detail = " + serviceName
                + ".selectTableById(connection, bean);\n            return JsonResponse.getSuccessJson(\"success\", detail);\n        }\n";
        return "\n\n    @RequestUri(\"/" + prefix + "/detail\")\n    public JsonResponse " + prefix
                + "Detail(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        "
                + upperTableName + " bean = getRequestParam(request, " + upperTableName + ".class);\n" +
                detailContent + "        return JsonResponse.getFailedJson(\"failed\");\n    }";
    }

    private String getDelete(String prefix, String upperTableName, boolean hasGlobalId, String serviceName) {
        String deleteContent = hasGlobalId
                ? "        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {\n            " + serviceName
                + ".softDeleteTableByGidList(connection, vo.getGidList());\n            return JsonResponse.getSuccessJson(\"success\");\n        }\n"
                : "        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {\n            " + serviceName
                + ".softDeleteTableByIdList(connection, vo.getIdList());\n            return JsonResponse.getSuccessJson(\"success\");\n        }\n";
        return "\n\n    @RequestUri(\"/" + prefix + "/delete\")\n    public JsonResponse " + prefix
                + "Delete(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {\n        "
                + upperTableName + "VO vo = getRequestParam(request, " + upperTableName + "VO.class);\n" +
                deleteContent + "        return JsonResponse.getFailedJson(\"failed\");\n    }";
    }
}
