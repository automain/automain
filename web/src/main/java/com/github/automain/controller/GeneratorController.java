package com.github.automain.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.ColumnBean;
import com.github.automain.common.bean.GeneratorVO;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.common.generator.BeanGenerator;
import com.github.automain.common.generator.CommonGenerator;
import com.github.automain.common.generator.ControllerGenerator;
import com.github.automain.common.generator.DaoGenerator;
import com.github.automain.common.generator.ServiceGenerator;
import com.github.automain.common.generator.VOGenerator;
import com.github.automain.util.DateUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import org.apache.commons.collections4.CollectionUtils;
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
        GeneratorVO vo = getRequestParam(request, GeneratorVO.class);
        if (vo != null) {
            List<String> tableNameList = CommonGenerator.selectTableNameList(connection, vo.getDatabaseName());
            return JsonResponse.getSuccessJson("success", tableNameList);
        } else {
            return JsonResponse.getFailedJson("failed");
        }
    }

    @RequestUri("/column/list")
    public JsonResponse columnList(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        GeneratorVO vo = getRequestParam(request, GeneratorVO.class);
        if (vo != null) {
            String databaseName = vo.getDatabaseName();
            String tableName = vo.getTableName();
            List<ColumnBean> columnList = CommonGenerator.selectNoPriColunmList(connection, databaseName, tableName);
            ServiceGenerator generator = new ServiceGenerator();
            String upperTableName = CommonGenerator.convertToJavaName(tableName, true);
            String serviceName = upperTableName + "Service";
            String paramName = tableName.toUpperCase() + "_SERVICE";
            String serviceContainerStr = serviceName + " " + paramName + " = new "
                    + serviceName + "(new " + upperTableName + "(), new " + upperTableName + "Dao());";
            BeanGenerator beanGenerator = new BeanGenerator();
            String bean = beanGenerator.generate(columnList, tableName, upperTableName);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("columnList", columnList);
            data.put("serviceContainerStr", serviceContainerStr);
            data.put("bean", bean);
            return JsonResponse.getSuccessJson("success", data);
        } else {
            return JsonResponse.getFailedJson("failed");
        }
    }

    @RequestUri("/generate")
    public JsonResponse generate(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GeneratorVO generatorVO = getRequestParam(request, GeneratorVO.class);
        if (generatorVO != null) {
            String databaseName = generatorVO.getDatabaseName();
            String tableName = generatorVO.getTableName();

            if (databaseName == null || tableName == null) {
                return JsonResponse.getFailedJson("failed");
            }
            String prefix = generatorVO.getPrefix();
            List<ColumnBean> columns = CommonGenerator.selectAllColumnList(connection, databaseName, tableName);
            List<String> keyColumns = CommonGenerator.selectKeyColumnList(connection, databaseName, tableName);
            String upperTableName = CommonGenerator.convertToJavaName(tableName, true);
            String upperPrefix = CommonGenerator.convertToJavaName(prefix, true);

            List<String> listCheck = generatorVO.getListCheck();
            List<String> addCheck = generatorVO.getAddCheck();
            List<String> updateCheck = generatorVO.getUpdateCheck();
            List<String> detailCheck = generatorVO.getDetailCheck();
            List<String> searchCheck = generatorVO.getSearchCheck();
            boolean hasAdd = CollectionUtils.isNotEmpty(addCheck);
            boolean hasUpdate = CollectionUtils.isNotEmpty(updateCheck);
            boolean hasDetail = CollectionUtils.isNotEmpty(detailCheck);
            boolean hasIsValid = CommonGenerator.hasIsValid(columns);
            boolean hasGlobalId = CommonGenerator.hasGlobalId(columns);
            boolean hasCreateTime = CommonGenerator.hasCreateTime(columns);
            boolean hasUpdateTime = CommonGenerator.hasUpdateTime(columns);

            String now = DateUtil.getNow("yyyyMMddHHmmss");

            BeanGenerator beanGenerator = new BeanGenerator();
            String bean = beanGenerator.generate(columns, tableName, upperTableName);
            CommonGenerator.generateFile(bean, now + "/bean/" + upperTableName + JAVA_LABEL);

            VOGenerator voGenerator = new VOGenerator();
            String vo = voGenerator.generate(columns, upperTableName, hasGlobalId);
            CommonGenerator.generateFile(vo, now + "/vo/" + upperTableName + "VO" + JAVA_LABEL);

            DaoGenerator daoGenerator = new DaoGenerator();
            String dao = daoGenerator.generate(columns, keyColumns, tableName, upperTableName, hasIsValid);
            CommonGenerator.generateFile(dao, now + "/dao/" + upperTableName + "Dao" + JAVA_LABEL);

            ServiceGenerator serviceGenerator = new ServiceGenerator();
            String service = serviceGenerator.generate(upperTableName);
            CommonGenerator.generateFile(service, now + "/service/" + upperTableName + "Service" + JAVA_LABEL);

            ControllerGenerator controllerGenerator = new ControllerGenerator();
            String controller = controllerGenerator.generate(prefix, upperPrefix, upperTableName, hasIsValid, hasGlobalId, hasCreateTime, hasUpdateTime, tableName);
            CommonGenerator.generateFile(controller, now + "/controller/" + upperPrefix + "Controller" + JAVA_LABEL);


//            ListViewGenerator listViewGenerator = new ListViewGenerator();
//            String listView = listViewGenerator.generate(databaseName, tableName, listCheck, dictionaryCheck, hasIsValid, hasUpdate, hasDetail);
//            CommonGenerator.generateFile(listView, now + "/" + executorPrefix + "/" + executorPrefix + "_list" + JSP_LABEL);
//
//            TabViewGenerator tabViewGenerator = new TabViewGenerator();
//            String tabView = tabViewGenerator.generate(databaseName, tableName, executorPrefix, listCheck, hasIsValid, hasAdd, hasUpdate, hasDetail, searchCheck, dictionaryCheck);
//            CommonGenerator.generateFile(tabView, now + "/" + executorPrefix + "/" + executorPrefix + "_tab" + JSP_LABEL);
//
//            AddViewGenerator addViewGenerator = new AddViewGenerator();
//            String addView = addViewGenerator.generate(databaseName, tableName, executorPrefix, dictionaryCheck, addCheck);
//            CommonGenerator.generateFile(addView, now + "/" + executorPrefix + "/" + executorPrefix + "_add" + JSP_LABEL);
//
//            UpdateViewGenerator updateViewGenerator = new UpdateViewGenerator();
//            String updateView = updateViewGenerator.generate(databaseName, tableName, executorPrefix, dictionaryCheck, updateCheck);
//            CommonGenerator.generateFile(updateView, now + "/" + executorPrefix + "/" + executorPrefix + "_update" + JSP_LABEL);
//
//            DetailViewGenerator detailViewGenerator = new DetailViewGenerator();
//            String detailView = detailViewGenerator.generate(databaseName, tableName, dictionaryCheck, detailCheck);
//            CommonGenerator.generateFile(detailView, now + "/" + executorPrefix + "/" + executorPrefix + "_detail" + JSP_LABEL);

            String compressPath = CommonGenerator.GENERATE_PATH + now;
            String zipPath = CommonGenerator.GENERATE_PATH + now + ZIP_LABEL;
            CommonGenerator.zipCompress(compressPath, zipPath);
            try {
                CommonGenerator.downloadFile(request, response, zipPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonResponse.getSuccessJson("success");
        } else {
            return JsonResponse.getFailedJson("failed");
        }
    }
}
