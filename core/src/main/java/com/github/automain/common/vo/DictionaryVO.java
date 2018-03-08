package com.github.automain.common.vo;

public class DictionaryVO {

    // 字典名
    private String dictionaryName;

    // 字典值
    private String dictionaryValue;

    // 父级ID
    private Long parentId;

    public DictionaryVO() {
    }

    public DictionaryVO(String dictionaryName, String dictionaryValue, Long parentId) {
        this.dictionaryName = dictionaryName;
        this.dictionaryValue = dictionaryValue;
        this.parentId = parentId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
