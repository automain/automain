package com.github.automain.common.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbConfig extends RequestUtil implements BaseBean<TbConfig> {

    // 配置ID
    private Long configId;

    // 配置key
    private String configKey;

    // 配置value
    private String configValue;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_config";
    }

    @Override
    public String primaryKey() {
        return "config_id";
    }

    @Override
    public Long primaryValue() {
        return this.getConfigId();
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getConfigKey() != null) {
            map.put("config_key", this.getConfigKey());
        }
        if (this.getConfigValue() != null) {
            map.put("config_value", this.getConfigValue());
        }
        if (this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbConfig beanFromResultSet(ResultSet rs) throws SQLException {
        TbConfig bean = new TbConfig();
        bean.setConfigId(rs.getLong("config_id"));
        bean.setConfigKey(rs.getString("config_key"));
        bean.setConfigValue(rs.getString("config_value"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbConfig beanFromRequest(HttpServletRequest request) {
        TbConfig bean = new TbConfig();
        bean.setConfigId(getLong("configId", request));
        bean.setConfigKey(getString("configKey", request));
        bean.setConfigValue(getString("configValue", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}