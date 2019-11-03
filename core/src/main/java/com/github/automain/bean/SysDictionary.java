package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysDictionary implements BaseBean<SysDictionary> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // 表名
    private String tableName;
    // 字段名
    private String columnName;
    // 字典键
    private Integer dictionaryKey;
    // 字典值
    private String dictionaryValue;

    public Integer getId() {
        return id;
    }

    public SysDictionary setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysDictionary setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysDictionary setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysDictionary setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public SysDictionary setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public SysDictionary setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public Integer getDictionaryKey() {
        return dictionaryKey;
    }

    public SysDictionary setDictionaryKey(Integer dictionaryKey) {
        this.dictionaryKey = dictionaryKey;
        return this;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public SysDictionary setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_dictionary";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(8);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
        }
        if (all || this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (all || this.getUpdateTime() != null) {
            map.put("update_time", this.getUpdateTime());
        }
        if (all || this.getIsValid() != null) {
            map.put("is_valid", this.getIsValid());
        }
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
    public SysDictionary beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysDictionary()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setTableName(rs.getString("table_name"))
                .setColumnName(rs.getString("column_name"))
                .setDictionaryKey(rs.getInt("dictionary_key"))
                .setDictionaryValue(rs.getString("dictionary_value"));
    }

    @Override
    public String toString() {
        return "SysDictionary{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", dictionaryKey=" + dictionaryKey +
                ", dictionaryValue='" + dictionaryValue + '\'' +
                '}';
    }
}