package com.github.automain.common.bean;

import java.util.List;

public class GeneratorVO {

    // 数据库名
    private String databaseName;
    // 表名
    private String tableName;
    // 业务模块名
    private String prefix;
    // 列表展示字段
    private List<String> listCheck;
    // 添加页面字段
    private List<String> addCheck;
    // 更新页面字段
    private List<String> updateCheck;
    // 详情展示字段
    private List<String> detailCheck;
    // 搜索字段
    private List<String> searchCheck;

    public String getDatabaseName() {
        return databaseName;
    }

    public GeneratorVO setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public GeneratorVO setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public GeneratorVO setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public List<String> getListCheck() {
        return listCheck;
    }

    public GeneratorVO setListCheck(List<String> listCheck) {
        this.listCheck = listCheck;
        return this;
    }

    public List<String> getAddCheck() {
        return addCheck;
    }

    public GeneratorVO setAddCheck(List<String> addCheck) {
        this.addCheck = addCheck;
        return this;
    }

    public List<String> getUpdateCheck() {
        return updateCheck;
    }

    public GeneratorVO setUpdateCheck(List<String> updateCheck) {
        this.updateCheck = updateCheck;
        return this;
    }

    public List<String> getDetailCheck() {
        return detailCheck;
    }

    public GeneratorVO setDetailCheck(List<String> detailCheck) {
        this.detailCheck = detailCheck;
        return this;
    }

    public List<String> getSearchCheck() {
        return searchCheck;
    }

    public GeneratorVO setSearchCheck(List<String> searchCheck) {
        this.searchCheck = searchCheck;
        return this;
    }
}
