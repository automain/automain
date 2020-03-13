package com.github.automain.common.generator;

import com.github.automain.common.bean.ColumnBean;
import com.github.fastjdbc.BaseDao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GeneratorDao extends BaseDao {

    public static List<String> selectDatabaseNameList() {
        List<String> databaseNameList = new ArrayList<String>();
        try (ResultSet rs = executeSelectReturnResultSet("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA", null)) {
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

    public static List<String> selectTableNameList(String databaseName) {
        List<String> tableNameList = new ArrayList<String>();
        try (ResultSet rs = executeSelectReturnResultSet("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + databaseName + "'", null)) {
            while (rs.next()) {
                tableNameList.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNameList;
    }

    public static List<ColumnBean> selectAllColumnList(String databaseName, String tableName) {
        List<ColumnBean> columnList = new ArrayList<ColumnBean>();
        try (ResultSet rs = executeSelectReturnResultSet("SELECT c.COLUMN_NAME, c.DATA_TYPE, c.COLUMN_COMMENT, c.COLUMN_KEY, c.EXTRA, c.CHARACTER_MAXIMUM_LENGTH, c.IS_NULLABLE FROM information_schema.COLUMNS c WHERE c.TABLE_SCHEMA = '" + databaseName + "' AND c.TABLE_NAME = '" + tableName + "'" + " ORDER BY c.ORDINAL_POSITION", null)) {
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
                bean.setIsNullAble("YES".equals(rs.getString("IS_NULLABLE")));
                columnList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnList;
    }


    public static List<String> selectKeyColumnList(String databaseName, String tableName) {
        List<String> keyColumnList = new ArrayList<String>();
        try (ResultSet rs = executeSelectReturnResultSet("SELECT st.COLUMN_NAME FROM information_schema.STATISTICS st WHERE st.TABLE_SCHEMA = '" + databaseName + "' AND st.TABLE_NAME = '" + tableName + "' AND st.INDEX_NAME != 'PRIMARY' ORDER BY st.INDEX_NAME,st.SEQ_IN_INDEX", null)) {
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
}
