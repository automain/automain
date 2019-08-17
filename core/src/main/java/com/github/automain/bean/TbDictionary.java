package com.github.automain.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbDictionary extends RequestUtil implements BaseBean<TbDictionary> {

    // 字典表ID
    private Long dictionaryId;

    // 表名
    private String dictTableName;

    // 字段名
    private String dictColumnName;

    // 字典名
    private String dictionaryName;

    // 字典值
    private String dictionaryValue;

    // 排序标识
    private Integer sequenceNumber;

    // 父级ID
    private Long parentId;

    // 是否是叶子节点(0:否,1:是)
    private Integer isLeaf;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========

    // 父级名称
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    // ========== additional column end ==========

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getDictTableName() {
        return dictTableName;
    }

    public void setDictTableName(String dictTableName) {
        this.dictTableName = dictTableName;
    }

    public String getDictColumnName() {
        return dictColumnName;
    }

    public void setDictColumnName(String dictColumnName) {
        this.dictColumnName = dictColumnName;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public void setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_dictionary";
    }

    @Override
    public String primaryKey() {
        return "dictionary_id";
    }

    @Override
    public Long primaryValue() {
        return this.getDictionaryId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getDictColumnName() != null) {
            map.put("dict_column_name", this.getDictColumnName());
        }
        if (all || this.getDictTableName() != null) {
            map.put("dict_table_name", this.getDictTableName());
        }
        if (all || this.getDictionaryName() != null) {
            map.put("dictionary_name", this.getDictionaryName());
        }
        if (all || this.getDictionaryValue() != null) {
            map.put("dictionary_value", this.getDictionaryValue());
        }
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        if (all || this.getIsLeaf() != null) {
            map.put("is_leaf", this.getIsLeaf());
        }
        if (all || this.getParentId() != null) {
            map.put("parent_id", this.getParentId());
        }
        if (all || this.getSequenceNumber() != null) {
            map.put("sequence_number", this.getSequenceNumber());
        }
        return map;
    }

    @Override
    public TbDictionary beanFromResultSet(ResultSet rs) throws SQLException {
        TbDictionary bean = new TbDictionary();
        bean.setDictionaryId(rs.getLong("dictionary_id"));
        bean.setDictTableName(rs.getString("dict_table_name"));
        bean.setDictColumnName(rs.getString("dict_column_name"));
        bean.setDictionaryName(rs.getString("dictionary_name"));
        bean.setDictionaryValue(rs.getString("dictionary_value"));
        bean.setSequenceNumber(rs.getInt("sequence_number"));
        bean.setParentId(rs.getLong("parent_id"));
        bean.setIsLeaf(rs.getInt("is_leaf"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbDictionary beanFromRequest(HttpServletRequest request) {
        TbDictionary bean = new TbDictionary();
        bean.setDictionaryId(getLong("dictionaryId", request));
        bean.setDictTableName(getString("dictTableName", request));
        bean.setDictColumnName(getString("dictColumnName", request));
        bean.setDictionaryName(getString("dictionaryName", request));
        bean.setDictionaryValue(getString("dictionaryValue", request));
        bean.setSequenceNumber(getInt("sequenceNumber", request));
        bean.setParentId(getLong("parentId", request));
        bean.setIsLeaf(getInt("isLeaf", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}