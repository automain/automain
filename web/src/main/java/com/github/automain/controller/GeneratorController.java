package com.github.automain.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.ColumnBean;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.common.generator.AddExecutorGenerator;
import com.github.automain.common.generator.AddViewGenerator;
import com.github.automain.common.generator.BeanGenerator;
import com.github.automain.common.generator.CommonGenerator;
import com.github.automain.common.generator.DaoGenerator;
import com.github.automain.common.generator.DeleteExecutorGenerator;
import com.github.automain.common.generator.DetailViewGenerator;
import com.github.automain.common.generator.ForwardExecutorGenerator;
import com.github.automain.common.generator.ListExecutorGenerator;
import com.github.automain.common.generator.ListViewGenerator;
import com.github.automain.common.generator.ServiceGenerator;
import com.github.automain.common.generator.TabViewGenerator;
import com.github.automain.common.generator.UpdateExecutorGenerator;
import com.github.automain.common.generator.UpdateViewGenerator;
import com.github.automain.util.DateUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.util.RequestUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorController extends BaseController {
    private static final String JAVA_LABEL = ".java";
    private static final String JSP_LABEL = ".jsp";
    private static final String ZIP_LABEL = ".zip";

    @RequestUri("/database/list")
    public JsonResponse databaseList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        return JsonResponse.getSuccessJson("success", CommonGenerator.selectDatabaseNameList(connection));
    }

    @RequestUri("/table/list")
    public JsonResponse tableList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        String databaseName = RequestUtil.getString("databaseName", request);
        List<String> tableNameList = CommonGenerator.selectTableNameList(connection, databaseName);
        return JsonResponse.getSuccessJson("success", tableNameList);
    }

    @RequestUri("/column/list")
    public JsonResponse columnList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        String databaseName = RequestUtil.getString("databaseName", request);
        String tableName = RequestUtil.getString("tableName", request);
        List<ColumnBean> columnList = CommonGenerator.selectNoPriColunmList(connection, databaseName, tableName);
        ServiceGenerator generator = new ServiceGenerator();
        String upperTableName = generator.convertToJavaName(tableName, true);
        String serviceName = upperTableName + "Service";
        String paramName = tableName + "_service";
        String serviceContainerStr = serviceName + " " + paramName.toUpperCase() + " = new "
                + serviceName + "(new " + upperTableName + "(), new " + upperTableName + "Dao());";
        BeanGenerator beanGenerator = new BeanGenerator();
        String bean = beanGenerator.generate(databaseName, tableName);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("columnList", columnList);
        data.put("serviceContainerStr", serviceContainerStr);
        data.put("bean", bean);
        return JsonResponse.getSuccessJson("success", data);
    }

    @RequestUri("/generate")
    public JsonResponse generate(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String databaseName = RequestUtil.getString("databaseName", request);
        String tableName = RequestUtil.getString("tableName", request);

        String[] listCheck = RequestUtil.getStringValues("listCheck", request);
        String[] dictionaryCheck = RequestUtil.getStringValues("dictionaryCheck", request);

        String[] addCheck = RequestUtil.getStringValues("addCheck", request);
        String[] updateCheck = RequestUtil.getStringValues("updateCheck", request);
        String[] detailCheck = RequestUtil.getStringValues("detailCheck", request);
        String[] searchCheck = RequestUtil.getStringValues("searchCheck", request);
        boolean hasAdd = addCheck != null && addCheck.length > 0;
        boolean hasUpdate = updateCheck != null && updateCheck.length > 0;
        boolean hasDetail = detailCheck != null && detailCheck.length > 0;
        boolean deleteCheck = CommonGenerator.hasIsValid(connection, databaseName, tableName);

        String now = DateUtil.getNow("yyyyMMddHHmmss");

        String executorPrefix = "";
        BeanGenerator beanGenerator = new BeanGenerator();
        String upperTableName = beanGenerator.convertToJavaName(tableName, true);
        String bean = beanGenerator.generate(databaseName, tableName);
        CommonGenerator.generateFile(bean, now + "/bean/" + upperTableName + JAVA_LABEL);

        DaoGenerator daoGenerator = new DaoGenerator();
        String dao = daoGenerator.generate(databaseName, tableName, deleteCheck);
        CommonGenerator.generateFile(dao, now + "/dao/" + upperTableName + "Dao" + JAVA_LABEL);

        ServiceGenerator serviceGenerator = new ServiceGenerator();
        String service = serviceGenerator.generate(databaseName, tableName);
        CommonGenerator.generateFile(service, now + "/service/" + upperTableName + "Service" + JAVA_LABEL);

        String upperPrefix = beanGenerator.convertToJavaName(executorPrefix, true);

        ForwardExecutorGenerator forwardExecutorGenerator = new ForwardExecutorGenerator();
        String forwardExecutor = forwardExecutorGenerator.generate(databaseName, tableName, dictionaryCheck, executorPrefix);
        CommonGenerator.generateFile(forwardExecutor, now + "/executor/view/" + upperPrefix + "ForwardExecutor" + JAVA_LABEL);

        ListExecutorGenerator listExecutorGenerator = new ListExecutorGenerator();
        String listExecutor = listExecutorGenerator.generate(tableName, executorPrefix, dictionaryCheck);
        CommonGenerator.generateFile(listExecutor, now + "/executor/view/" + upperPrefix + "ListExecutor" + JAVA_LABEL);

        AddExecutorGenerator addExecutorGenerator = new AddExecutorGenerator();
        String addExecutor = addExecutorGenerator.generate(tableName, executorPrefix, hasAdd);
        CommonGenerator.generateFile(addExecutor, now + "/executor/action/" + upperPrefix + "AddExecutor" + JAVA_LABEL);

        UpdateExecutorGenerator updateExecutorGenerator = new UpdateExecutorGenerator();
        String updateExecutor = updateExecutorGenerator.generate(tableName, executorPrefix, hasUpdate);
        CommonGenerator.generateFile(updateExecutor, now + "/executor/action/" + upperPrefix + "UpdateExecutor" + JAVA_LABEL);

        if (deleteCheck) {
            DeleteExecutorGenerator deleteExecutorGenerator = new DeleteExecutorGenerator();
            String deleteExecutor = deleteExecutorGenerator.generate(databaseName, tableName, executorPrefix);
            CommonGenerator.generateFile(deleteExecutor, now + "/executor/action/" + upperPrefix + "DeleteExecutor" + JAVA_LABEL);
        }

        ListViewGenerator listViewGenerator = new ListViewGenerator();
        String listView = listViewGenerator.generate(databaseName, tableName, listCheck, dictionaryCheck, deleteCheck, hasUpdate, hasDetail);
        CommonGenerator.generateFile(listView, now + "/" + executorPrefix + "/" + executorPrefix + "_list" + JSP_LABEL);

        TabViewGenerator tabViewGenerator = new TabViewGenerator();
        String tabView = tabViewGenerator.generate(databaseName, tableName, executorPrefix, listCheck, deleteCheck, hasAdd, hasUpdate, hasDetail, searchCheck, dictionaryCheck);
        CommonGenerator.generateFile(tabView, now + "/" + executorPrefix + "/" + executorPrefix + "_tab" + JSP_LABEL);

        AddViewGenerator addViewGenerator = new AddViewGenerator();
        String addView = addViewGenerator.generate(databaseName, tableName, executorPrefix, dictionaryCheck, addCheck);
        CommonGenerator.generateFile(addView, now + "/" + executorPrefix + "/" + executorPrefix + "_add" + JSP_LABEL);

        UpdateViewGenerator updateViewGenerator = new UpdateViewGenerator();
        String updateView = updateViewGenerator.generate(databaseName, tableName, executorPrefix, dictionaryCheck, updateCheck);
        CommonGenerator.generateFile(updateView, now + "/" + executorPrefix + "/" + executorPrefix + "_update" + JSP_LABEL);

        DetailViewGenerator detailViewGenerator = new DetailViewGenerator();
        String detailView = detailViewGenerator.generate(databaseName, tableName, dictionaryCheck, detailCheck);
        CommonGenerator.generateFile(detailView, now + "/" + executorPrefix + "/" + executorPrefix + "_detail" + JSP_LABEL);

        String compressPath = CommonGenerator.GENERATE_PATH + now;
        String zipPath = CommonGenerator.GENERATE_PATH + now + ZIP_LABEL;
        CommonGenerator.zipCompress(compressPath, zipPath);
        try {
            CommonGenerator.downloadFile(request, response, zipPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResponse.getSuccessJson("success");
    }

    // ============================== 查询数据库方法 ==============================


}
