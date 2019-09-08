package com.github.automain.vo;

import com.github.fastjdbc.common.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DictionaryVO implements BaseBean<DictionaryVO> {

    // 表名
    private String tableName;
    // 字段名
    private String columnName;
    // 字典键
    private Integer dictionaryKey;
    // 字典值
    private String dictionaryValue;

    public String getTableName() {
        return tableName;
    }

    public DictionaryVO setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public DictionaryVO setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public Integer getDictionaryKey() {
        return dictionaryKey;
    }

    public DictionaryVO setDictionaryKey(Integer dictionaryKey) {
        this.dictionaryKey = dictionaryKey;
        return this;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public DictionaryVO setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_dictionary";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(4);
        if (all || this.getTableName() != null) {
            map.put("table_name", this.getTableName());
        }
        if (all || this.getColumnName() != null) {
            map.put("column_name", this.getColumnName());
        }
        if (all || this.getDictionaryKey() != null) {
            map.put("dictionary_key", this.getDictionaryKey());
        }
        if (all || this.getDictionaryValue() != null) {
            map.put("dictionary_value", this.getDictionaryValue());
        }
        return map;
    }

    @Override
    public DictionaryVO beanFromResultSet(ResultSet rs) throws SQLException {
        return new DictionaryVO()
                .setTableName(rs.getString("table_name"))
                .setColumnName(rs.getString("column_name"))
                .setDictionaryKey(rs.getInt("dictionary_key"))
                .setDictionaryValue(rs.getString("dictionary_value"));
    }

    @Override
    public String toString() {
        return "DictionaryVO{" +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", dictionaryKey=" + dictionaryKey +
                ", dictionaryValue='" + dictionaryValue + '\'' +
                '}';
    }
}
