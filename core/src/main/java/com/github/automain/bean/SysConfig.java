package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysConfig implements BaseBean<SysConfig> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // 配置项键
    private String configKey;
    // 配置项值
    private String configValue;
    // 配置项描述
    private String configRemark;

    public Integer getId() {
        return id;
    }

    public SysConfig setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysConfig setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysConfig setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysConfig setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getConfigKey() {
        return configKey;
    }

    public SysConfig setConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public String getConfigValue() {
        return configValue;
    }

    public SysConfig setConfigValue(String configValue) {
        this.configValue = configValue;
        return this;
    }

    public String getConfigRemark() {
        return configRemark;
    }

    public SysConfig setConfigRemark(String configRemark) {
        this.configRemark = configRemark;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_config";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(7);
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
        if (all || this.getConfigKey() != null) {
            map.put("config_key", this.getConfigKey());
        }
        if (all || this.getConfigValue() != null) {
            map.put("config_value", this.getConfigValue());
        }
        if (all || this.getConfigRemark() != null) {
            map.put("config_remark", this.getConfigRemark());
        }
        return map;
    }

    @Override
    public SysConfig beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysConfig()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setConfigKey(rs.getString("config_key"))
                .setConfigValue(rs.getString("config_value"))
                .setConfigRemark(rs.getString("config_remark"));
    }

    @Override
    public String toString() {
        return "SysConfig{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", configRemark='" + configRemark + '\'' +
                '}';
    }
}