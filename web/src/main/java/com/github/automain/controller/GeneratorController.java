package com.github.automain.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.ColumnBean;
import com.github.automain.common.bean.GeneratorVO;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.generator.BeanGenerator;
import com.github.automain.common.generator.CommonGenerator;
import com.github.automain.common.generator.ControllerGenerator;
import com.github.automain.common.generator.DaoGenerator;
import com.github.automain.common.generator.ServiceGenerator;
import com.github.automain.common.generator.VOGenerator;
import com.github.automain.common.generator.ViewGenerator;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorController implements ServiceContainer {

    @RequestUri("/dev/databaseList")
    public JsonResponse databaseList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        return JsonResponse.getSuccessJson(selectDatabaseNameList(connection));
    }

    @RequestUri("/dev/tableList")
    public JsonResponse tableList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        GeneratorVO vo = SystemUtil.getRequestParam(request, GeneratorVO.class);
        if (vo != null) {
            List<String> tableNameList = selectTableNameList(connection, vo.getDatabaseName());
            return JsonResponse.getSuccessJson(tableNameList);
        } else {
            return JsonResponse.getFailedJson();
        }
    }

    @RequestUri("/dev/columnList")
    public JsonResponse columnList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) {
        GeneratorVO vo = SystemUtil.getRequestParam(request, GeneratorVO.class);
        if (vo != null) {
            String databaseName = vo.getDatabaseName();
            String tableName = vo.getTableName();
            List<ColumnBean> columnList = selectAllColumnList(connection, databaseName, tableName);
            String upperTableName = CommonGenerator.convertToJavaName(tableName, true);
            String serviceName = upperTableName + "Service";
            String paramName = tableName.toUpperCase() + "_SERVICE";
            String serviceContainer = serviceName + " " + paramName + " = new "
                    + serviceName + "(new " + upperTableName + "(), new " + upperTableName + "Dao());";
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
    public JsonResponse generate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GeneratorVO generatorVO = SystemUtil.getRequestParam(request, GeneratorVO.class);
        if (generatorVO != null) {
            String databaseName = generatorVO.getDatabaseName();
            String tableName = generatorVO.getTableName();

            if (databaseName == null || tableName == null) {
                return JsonResponse.getFailedJson();
            }
            String prefix = generatorVO.getPrefix();
            List<ColumnBean> columns = selectAllColumnList(connection, databaseName, tableName);
            List<String> keyColumns = selectKeyColumnList(connection, databaseName, tableName);
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

            List<String> dictionaryColumnList = SYS_DICTIONARY_SERVICE.selectDictionaryColumn(connection, tableName);

            String now = DateUtil.getNow("yyyyMMddHHmmss");

            BeanGenerator beanGenerator = new BeanGenerator();
            String bean = beanGenerator.generate(columns, tableName, upperTableName);
            generateFile(bean, now + "/bean/" + upperTableName + ".java");

            VOGenerator voGenerator = new VOGenerator();
            String vo = voGenerator.generate(columns, upperTableName, hasGlobalId, dictionaryColumnList);
            generateFile(vo, now + "/vo/" + upperTableName + "VO" + ".java");

            DaoGenerator daoGenerator = new DaoGenerator();
            String dao = daoGenerator.generate(columns, keyColumns, tableName, upperTableName, hasIsValid, dictionaryColumnList);
            generateFile(dao, now + "/dao/" + upperTableName + "Dao" + ".java");

            ServiceGenerator serviceGenerator = new ServiceGenerator();
            String service = serviceGenerator.generate(upperTableName);
            generateFile(service, now + "/service/" + upperTableName + "Service" + ".java");

            ControllerGenerator controllerGenerator = new ControllerGenerator();
            String controller = controllerGenerator.generate(prefix, upperPrefix, tableName, upperTableName, hasList, hasAdd, hasUpdate, hasDetail, hasIsValid, hasGlobalId, hasCreateTime, hasUpdateTime);
            generateFile(controller, now + "/controller/" + upperPrefix + "Controller" + ".java");

            ViewGenerator viewGenerator = new ViewGenerator();
            String view = viewGenerator.generate(columns, prefix, upperPrefix, tableName, upperTableName, listCheck, addCheck, updateCheck, detailCheck, keyColumns, sortCheck, hasIsValid, hasGlobalId, dictionaryColumnList);
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

    private List<String> selectDatabaseNameList(Connection connection) {
        List<String> databaseNameList = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA");
             ResultSet rs = statement.executeQuery()) {
            List<String> systemDatebaseList = List.of("information_schema", "mysql", "performance_schema", "sys");
            while (rs.next()) {
                String databaseName = rs.getString(1);
                if (!systemDatebaseList.contains(databaseName)) {
                    databaseNameList.add(databaseName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseNameList;
    }

    private List<String> selectTableNameList(Connection connection, String databaseName) {
        List<String> tableNameList = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + databaseName + "'");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                tableNameList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNameList;
    }

    private List<ColumnBean> selectAllColumnList(Connection connection, String databaseName, String tableName) {
        List<ColumnBean> columnList = new ArrayList<ColumnBean>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT c.COLUMN_NAME, c.DATA_TYPE, c.COLUMN_COMMENT, c.COLUMN_KEY, c.EXTRA, c.CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS c WHERE c.TABLE_SCHEMA = '" + databaseName + "' AND c.TABLE_NAME = '" + tableName + "'" + " ORDER BY c.ORDINAL_POSITION");
             ResultSet rs = statement.executeQuery()) {
            ColumnBean bean = null;
            List<String> textAreaColumnList = List.of("longtext", "mediumtext", "text", "tinytext");
            while (rs.next()) {
                bean = new ColumnBean();
                bean.setColumnName(rs.getString("COLUMN_NAME"));
                String dataType = rs.getString("DATA_TYPE");
                long maxLength = rs.getLong("CHARACTER_MAXIMUM_LENGTH");
                if ((("varchar".equals(dataType) || "char".equals(dataType)) && maxLength > 128L) || textAreaColumnList.contains(dataType)) {
                    bean.setIsTextArea(true);
                } else {
                    bean.setIsTextArea(false);
                }
                bean.setDataType(getDataType(dataType));
                bean.setColumnComment(rs.getString("COLUMN_COMMENT"));
                bean.setColumnKey(rs.getString("COLUMN_KEY"));
                bean.setExtra(rs.getString("EXTRA"));
                columnList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnList;
    }


    private List<String> selectKeyColumnList(Connection connection, String databaseName, String tableName) {
        List<String> keyColumnList = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT st.COLUMN_NAME FROM information_schema.STATISTICS st WHERE st.TABLE_SCHEMA = '" + databaseName + "' AND st.TABLE_NAME = '" + tableName + "' AND st.INDEX_NAME != 'PRIMARY' ORDER BY st.INDEX_NAME,st.SEQ_IN_INDEX");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                keyColumnList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyColumnList;
    }

    private String getDataType(String data_type) {
        switch (data_type) {
            case "bigint":
                return "Long";
            case "binary":
            case "blob":
            case "longblob":
            case "mediumblob":
            case "tinyblob":
            case "varbinary":
                return "byte[]";
            case "bit":
            case "bool":
            case "boolean":
                return "Boolean";
            case "char":
            case "enum":
            case "longtext":
            case "mediumtext":
            case "set":
            case "text":
            case "tinytext":
            case "varchar":
                return "String";
            case "date":
            case "year":
                return "Date";
            case "datetime":
            case "timestamp":
                return "Timestamp";
            case "decimal":
                return "BigDecimal";
            case "double":
            case "numeric":
            case "real":
                return "Double";
            case "float":
                return "Float";
            case "int":
            case "mediumint":
            case "smallint":
            case "tinyint":
                return "Integer";
            case "time":
                return "Time";
            default:
                return null;
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
