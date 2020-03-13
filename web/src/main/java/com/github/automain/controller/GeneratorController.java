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
import com.github.automain.common.generator.GeneratorDao;
import com.github.automain.common.generator.ServiceGenerator;
import com.github.automain.common.generator.VOGenerator;
import com.github.automain.common.generator.ViewGenerator;
import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
import com.github.automain.vo.SysDictionaryVO;
import org.apache.commons.collections4.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorController extends BaseController {

    @RequestUri("/dev/databaseList")
    public JsonResponse databaseList() {
        return JsonResponse.getSuccessJson(GeneratorDao.selectDatabaseNameList());
    }

    @RequestUri("/dev/tableList")
    public JsonResponse tableList(GeneratorVO vo) {
        if (vo != null) {
            List<String> tableNameList = GeneratorDao.selectTableNameList(vo.getDatabaseName());
            return JsonResponse.getSuccessJson(tableNameList);
        } else {
            return JsonResponse.getFailedJson();
        }
    }

    @RequestUri("/dev/appTableList")
    public JsonResponse appTableList() {
        List<String> tableNameList = GeneratorDao.selectTableNameList(SystemUtil.DATABASE_NAME);
        return JsonResponse.getSuccessJson(tableNameList);
    }

    @RequestUri("/dev/appColumnList")
    public JsonResponse appColumnList(SysDictionaryVO vo) {
        if (vo != null) {
            List<ColumnBean> columnList = GeneratorDao.selectAllColumnList(SystemUtil.DATABASE_NAME, vo.getTableName());
            return JsonResponse.getSuccessJson(columnList);
        } else {
            return JsonResponse.getFailedJson();
        }
    }

    @RequestUri("/dev/columnList")
    public JsonResponse columnList(GeneratorVO vo) {
        if (vo != null) {
            String databaseName = vo.getDatabaseName();
            String tableName = vo.getTableName();
            List<ColumnBean> columnList = GeneratorDao.selectAllColumnList(databaseName, tableName);
            String upperTableName = CommonGenerator.convertToJavaName(tableName, true);
            String serviceName = upperTableName + "Service";
            String servicePropertyName = tableName.toUpperCase() + "_SERVICE";
            String serviceContainer = serviceName + " " + servicePropertyName + " = new " + serviceName + "();";
            BeanGenerator beanGenerator = new BeanGenerator();
            String bean = beanGenerator.generate(columnList, tableName, upperTableName);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("columnList", columnList);
            data.put("serviceContainer", serviceContainer);
            data.put("bean", bean);
            return JsonResponse.getSuccessJson(data);
        } else {
            return JsonResponse.getFailedJson();
        }
    }

    @RequestUri("/dev/generate")
    public JsonResponse generate(HttpServletRequest request, HttpServletResponse response, GeneratorVO generatorVO) throws Exception {
        if (generatorVO != null) {
            String databaseName = generatorVO.getDatabaseName();
            String tableName = generatorVO.getTableName();

            if (databaseName == null || tableName == null) {
                return JsonResponse.getFailedJson();
            }
            String prefix = generatorVO.getPrefix();
            List<ColumnBean> columns = GeneratorDao.selectAllColumnList(databaseName, tableName);
            List<String> keyColumns = GeneratorDao.selectKeyColumnList(databaseName, tableName);
            String upperTableName = CommonGenerator.convertToJavaName(tableName, true);
            String upperPrefix = CommonGenerator.convertToJavaName(prefix, true);

            List<String> listCheck = generatorVO.getListCheck();
            List<String> addCheck = generatorVO.getAddCheck();
            List<String> updateCheck = generatorVO.getUpdateCheck();
            List<String> detailCheck = generatorVO.getDetailCheck();
            List<String> sortCheck = generatorVO.getSortCheck();
            boolean hasList = CollectionUtils.isNotEmpty(listCheck);
            boolean hasAdd = CollectionUtils.isNotEmpty(addCheck);
            boolean hasUpdate = CollectionUtils.isNotEmpty(updateCheck);
            boolean hasDetail = CollectionUtils.isNotEmpty(detailCheck);
            boolean hasIsValid = hasIsValid(columns);
            boolean hasGlobalId = hasGlobalId(columns);
            boolean hasCreateTime = hasCreateTime(columns);
            boolean hasUpdateTime = hasUpdateTime(columns);

            List<String> dictionaryColumnList = SysDictionaryDao.selectDictionaryColumn(tableName);

            String now = DateUtil.getNow("yyyyMMddHHmmss");

            BeanGenerator beanGenerator = new BeanGenerator();
            String bean = beanGenerator.generate(columns, tableName, upperTableName);
            generateFile(bean, now + "/bean/" + upperTableName + ".java");

            VOGenerator voGenerator = new VOGenerator();
            String vo = voGenerator.generate(columns, upperTableName, hasGlobalId, dictionaryColumnList, hasIsValid, keyColumns);
            generateFile(vo, now + "/vo/" + upperTableName + "VO" + ".java");

            DaoGenerator daoGenerator = new DaoGenerator();
            String dao = daoGenerator.generate(columns, keyColumns, tableName, upperTableName, hasIsValid, dictionaryColumnList, hasGlobalId);
            generateFile(dao, now + "/dao/" + upperTableName + "Dao" + ".java");

            ServiceGenerator serviceGenerator = new ServiceGenerator();
            String service = serviceGenerator.generate(upperTableName);
            generateFile(service, now + "/service/" + upperTableName + "Service" + ".java");

            ControllerGenerator controllerGenerator = new ControllerGenerator();
            String controller = controllerGenerator.generate(columns, prefix, upperPrefix, tableName, upperTableName, hasList, hasAdd, hasUpdate, hasDetail, hasIsValid, hasGlobalId, hasCreateTime, hasUpdateTime);
            generateFile(controller, now + "/controller/" + upperPrefix + "Controller" + ".java");

            ViewGenerator viewGenerator = new ViewGenerator();
            String view = viewGenerator.generate(columns, tableName, listCheck, addCheck, updateCheck, detailCheck, keyColumns, sortCheck, hasIsValid, hasGlobalId, dictionaryColumnList, prefix);
            generateFile(view, now + "/view/" + upperPrefix + ".vue");

            String compressPath = "/data/" + now;
            String zipPath = compressPath + ".zip";
            CompressUtil.zipCompress(compressPath, zipPath);
            try {
                HTTPUtil.downloadFile(request, response, zipPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonResponse.getSuccessJson();
        } else {
            return JsonResponse.getFailedJson();
        }
    }

    private void generateFile(String fileContent, String relativePath) throws IOException {
        if (fileContent != null) {
            File file = new File("/data/" + relativePath);
            File parentFile = file.getParentFile();
            if ((parentFile.exists() && parentFile.isDirectory()) || parentFile.mkdirs()) {
                try (OutputStream os = new FileOutputStream(file);
                     OutputStreamWriter writer = new OutputStreamWriter(os, PropertiesUtil.DEFAULT_CHARSET)) {
                    writer.write(fileContent);
                    writer.flush();
                }
            }
        }
    }
    private boolean hasIsValid(List<ColumnBean> columns) {
        for (ColumnBean column : columns) {
            if ("is_valid".equals(column.getColumnName()) && "Integer".equals(column.getDataType())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasGlobalId(List<ColumnBean> columns) {
        for (ColumnBean column : columns) {
            if ("gid".equals(column.getColumnName()) && "String".equals(column.getDataType())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCreateTime(List<ColumnBean> columns) {
        for (ColumnBean column : columns) {
            if ("create_time".equals(column.getColumnName()) && "Integer".equals(column.getDataType())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasUpdateTime(List<ColumnBean> columns) {
        for (ColumnBean column : columns) {
            if ("update_time".equals(column.getColumnName()) && "Integer".equals(column.getDataType())) {
                return true;
            }
        }
        return false;
    }

}
