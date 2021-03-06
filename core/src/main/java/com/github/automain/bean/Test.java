package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Test implements BaseBean<Test> {

    // 主键
    private Integer id;
    // 测试GID
    private String gid;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // 金额
    private BigDecimal money;
    // 备注
    private String remark;
    // 测试名称
    private String testName;
    // 测试字典(0:字典0,1:字典1,2:字典2)
    private Integer testDictionary;

    public Integer getId() {
        return id;
    }

    public Test setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public Test setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public Test setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public Test setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public Test setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Test setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Test setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public Test setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public Integer getTestDictionary() {
        return testDictionary;
    }

    public Test setTestDictionary(Integer testDictionary) {
        this.testDictionary = testDictionary;
        return this;
    }

    @Override
    public String tableName() {
        return "test";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(9);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
        }
        if (all || this.getGid() != null) {
            map.put("gid", this.getGid());
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
        if (all || this.getMoney() != null) {
            map.put("money", this.getMoney());
        }
        if (all || this.getRemark() != null) {
            map.put("remark", this.getRemark());
        }
        if (all || this.getTestName() != null) {
            map.put("test_name", this.getTestName());
        }
        if (all || this.getTestDictionary() != null) {
            map.put("test_dictionary", this.getTestDictionary());
        }
        return map;
    }

    @Override
    public Test beanFromResultSet(ResultSet rs) throws SQLException {
        return new Test()
                .setId(rs.getInt("id"))
                .setGid(rs.getString("gid"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setMoney(rs.getBigDecimal("money"))
                .setRemark(rs.getString("remark"))
                .setTestName(rs.getString("test_name"))
                .setTestDictionary(rs.getInt("test_dictionary"));
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", gid='" + gid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", money=" + money +
                ", remark='" + remark + '\'' +
                ", testName='" + testName + '\'' +
                ", testDictionary=" + testDictionary +
                '}';
    }
}