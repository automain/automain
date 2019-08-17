package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;
import com.github.fastjdbc.bean.ConnectionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CommonGenerator {

    protected static final String DELETE_LABEL_COLUMN_NAME = "is_valid";
    public static final String GENERATE_PATH = "/data/";
    private static final List<String> SYSTEM_DATABASE = Arrays.asList("information_schema", "mysql", "performance_schema", "sys");
    private static final List<String> TEXT_AREA_COLUMN = Arrays.asList("longtext", "mediumtext", "text", "tinytext");

    protected ColumnBean getPrimaryColumn(List<ColumnBean> columns) {
        if (columns == null || columns.size() == 0) {
            return null;
        }
        ColumnBean result = null;
        ColumnBean autoIncreaseCloumn = null;
        for (ColumnBean column : columns) {
            if ("PRI".equals(column.getColumnKey())) {
                result = column;
            }
            if ("auto_increment".equals(column.getExtra())) {
                autoIncreaseCloumn = column;
            }
        }
        if (result == null) {
            if (autoIncreaseCloumn == null) {
                return columns.get(0);
            } else {
                return autoIncreaseCloumn;
            }
        }
        return result;
    }

    protected List<ColumnBean> getMULColumn(List<ColumnBean> columns) {
        return getColumnByKeyType(columns, "MUL");
    }

    protected List<ColumnBean> getUNIColumn(List<ColumnBean> columns) {
        return getColumnByKeyType(columns, "UNI");
    }

    private List<ColumnBean> getColumnByKeyType(List<ColumnBean> columns, String keyType) {
        if (columns == null || columns.size() == 0) {
            return null;
        }
        List<ColumnBean> resultList = new ArrayList<ColumnBean>();
        for (ColumnBean column : columns) {
            if (keyType.equals(column.getColumnKey())) {
                resultList.add(column);
            }
        }
        return resultList;
    }

    public String convertToJavaName(String dbName, boolean uppercaseFirst) {
        String[] names = dbName.split("_");
        StringBuilder javaName = new StringBuilder();
        for (String name : names) {
            if (name.length() == 0) {
                continue;
            }
            javaName.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
        }
        if (uppercaseFirst) {
            return javaName.toString();
        } else {
            return javaName.substring(0, 1).toLowerCase() + javaName.substring(1);
        }
    }

    public String getJspHead() {
        return "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <%@include file=\"../common/common.jsp\" %>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body>\n";
    }

    protected String getTimeTypeImport(List<ColumnBean> columns) {
        Set<String> timeTypeSet = new HashSet<String>();
        for (ColumnBean column : columns) {
            String dataType = column.getDataType();
            if ("Timestamp".equals(dataType)) {
                timeTypeSet.add("import java.sql.Timestamp;\n");
            }
            if ("Time".equals(dataType)) {
                timeTypeSet.add("import java.sql.Time;\n");
            }
            if ("Date".equals(dataType)) {
                timeTypeSet.add("import java.util.Date;\n");
            }
        }
        StringBuilder timeTypeImport = new StringBuilder();
        for (String s : timeTypeSet) {
            timeTypeImport.append(s);
        }
        return timeTypeImport.toString();
    }

    protected boolean checkTimeTypeColumn(ColumnBean column) {
        return "Integer".equals(column.getDataType()) && column.getColumnName().endsWith("time");
    }

    public static List<ColumnBean> selectNoPriColunmList(ConnectionBean connection, String databaseName, String tableName) {
        List<ColumnBean> columnList = new ArrayList<ColumnBean>();
        try (PreparedStatement statement = connection.getReadConnection().prepareStatement("SELECT c.COLUMN_NAME, c.DATA_TYPE, c.COLUMN_COMMENT, c.COLUMN_KEY FROM information_schema.COLUMNS c WHERE c.TABLE_SCHEMA = '" + databaseName + "' AND c.TABLE_NAME = '" + tableName + "'" + " ORDER BY c.ORDINAL_POSITION");
             ResultSet rs = statement.executeQuery()) {
            ColumnBean bean = null;
            while (rs.next()) {
                if (!"PRI".equalsIgnoreCase(rs.getString("COLUMN_KEY"))) {
                    bean = new ColumnBean();
                    bean.setColumnName(rs.getString("COLUMN_NAME"));
                    bean.setDataType(rs.getString("DATA_TYPE"));
                    bean.setColumnComment(rs.getString("COLUMN_COMMENT"));
                    columnList.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnList;
    }

    public static List<String> selectDatabaseNameList(ConnectionBean connection) {
        List<String> databaseNameList = new ArrayList<String>();
        try (PreparedStatement statement = connection.getReadConnection().prepareStatement("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                String databaseName = rs.getString(1);
                if (!SYSTEM_DATABASE.contains(databaseName)) {
                    databaseNameList.add(databaseName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseNameList;
    }

    public static List<String> selectTableNameList(ConnectionBean connection, String databaseName) {
        List<String> tableNameList = new ArrayList<String>();
        try (PreparedStatement statement = connection.getReadConnection().prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + databaseName + "'");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                tableNameList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNameList;
    }

    public static List<ColumnBean> selectAllColumnList(ConnectionBean connection, String databaseName, String tableName) {
        List<ColumnBean> columnList = new ArrayList<ColumnBean>();
        try (PreparedStatement statement = connection.getReadConnection().prepareStatement("SELECT c.COLUMN_NAME, c.DATA_TYPE, c.COLUMN_COMMENT, c.COLUMN_KEY, c.EXTRA, c.CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS c WHERE c.TABLE_SCHEMA = '" + databaseName + "' AND c.TABLE_NAME = '" + tableName + "'" + " ORDER BY c.ORDINAL_POSITION");
             ResultSet rs = statement.executeQuery()) {
            ColumnBean bean = null;
            while (rs.next()) {
                bean = new ColumnBean();
                bean.setColumnName(rs.getString("COLUMN_NAME"));
                String dataType = rs.getString("DATA_TYPE");
                long maxLength = rs.getLong("CHARACTER_MAXIMUM_LENGTH");
                if ((("varchar".equals(dataType) || "char".equals(dataType)) && maxLength > 128L) || TEXT_AREA_COLUMN.contains(dataType)) {
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

    public static boolean hasIsValid(ConnectionBean connection, String databaseName, String tableName) {
        List<ColumnBean> columns = selectAllColumnList(connection, databaseName, tableName);
        for (ColumnBean column : columns) {
            if ("is_valid".equals(column.getColumnName()) && "Integer".equals(column.getDataType())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> selectKeyColumnList(ConnectionBean connection, String databaseName, String tableName) {
        List<String> keyColumnList = new ArrayList<String>();
        try (PreparedStatement statement = connection.getReadConnection().prepareStatement("SELECT st.COLUMN_NAME FROM information_schema.STATISTICS st WHERE st.TABLE_SCHEMA = '" + databaseName + "' AND st.TABLE_NAME = '" + tableName + "' AND st.INDEX_NAME != 'PRIMARY' ORDER BY st.INDEX_NAME,st.SEQ_IN_INDEX");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                keyColumnList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyColumnList;
    }

    private static String getDataType(String data_type) {
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


    public static void generateFile(String fileContent, String relativePath) throws IOException {
        if (fileContent != null) {
            File file = new File(GENERATE_PATH + relativePath);
            File parentFile = file.getParentFile();
            if ((parentFile.exists() && parentFile.isDirectory()) || parentFile.mkdirs()) {
                try (OutputStream os = new FileOutputStream(file);
                     OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8")) {
                    writer.write(fileContent);
                    writer.flush();
                }
            }
        }
    }

    public static void zipCompress(String filePath, String zipPath) throws IOException {
        ZipOutputStream zos = null;
        try {
            File file = new File(filePath);
            File zipFile = new File(zipPath);
            String parentPath = "";
            if (file.exists()) {
                parentPath = file.getParentFile().getPath();
                if (!parentPath.endsWith(File.separator)) {
                    parentPath += File.separator;
                }
            }
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zipDirectory(parentPath, file, zos);
        } catch (Exception e) {
            throw e;
        } finally {
            if (zos != null) {
                try {
                    zos.finish();
                    zos.close();
                    zos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void zipDirectory(String rootPath, File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                zos.putNextEntry(new ZipEntry(file.getPath().replace(rootPath, "") + "/"));
                zos.closeEntry();
            } else {
                for (File fileName : files) {
                    if (fileName.isDirectory()) {
                        zipDirectory(rootPath, fileName, zos);
                    } else {
                        zipFile(rootPath, zos, fileName);
                    }
                }
            }
        } else {
            zipFile(rootPath, zos, file);
        }
    }

    private static void zipFile(String rootPath, ZipOutputStream zos, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            zos.putNextEntry(new ZipEntry(file.getPath().replace(rootPath, "")));
            int readBytes = -1;
            byte[] buf = new byte[2048];
            while ((readBytes = fis.read(buf)) > 0) {
                zos.write(buf, 0, readBytes);
            }
            zos.closeEntry();
        }
    }

    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        File file = new File(path);
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            String userAgent = request.getHeader("User-Agent").toUpperCase();
            String parentPath = file.getParent();
            String fileName = parentPath == null ? path : file.getPath().replace(parentPath, "");
            if (userAgent.contains("EDGE") || userAgent.contains("MSIE") || userAgent.contains("RV:11")) {
                fileName = new String(Base64.getUrlEncoder().encode((fileName.getBytes("UTF-8"))), "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
            byte[] b = new byte[2048];
            int len;
            while ((len = is.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
        }

    }

}
