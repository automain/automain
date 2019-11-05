package com.github.automain.common.bean;

public class ColumnBean {

    private String columnName;

    private String dataType;

    private String columnComment;

    private String columnKey;

    private String extra;

    private Boolean isTextArea;

    private Boolean isNullAble;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Boolean getIsTextArea() {
        return isTextArea;
    }

    public void setIsTextArea(Boolean isTextArea) {
        this.isTextArea = isTextArea;
    }

    public Boolean getIsNullAble() {
        return isNullAble;
    }

    public void setIsNullAble(Boolean isNullAble) {
        this.isNullAble = isNullAble;
    }
}
