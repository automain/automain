package com.github.automain.common.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TbConfig extends RequestUtil implements BaseBean<TbConfig> {

    // 配置ID
    private Long configId;

    // 配置key
    private String configKey;

    // 配置value
    private String configValue;

    // 配置描述
    private String configComment;

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========

    // 创建时间
    private String createTimeRange;
    // 更新时间
    private String updateTimeRange;

    public String getCreateTimeRange() {
        return createTimeRange;
    }

    public void setCreateTimeRange(String createTimeRange) {
        this.createTimeRange = createTimeRange;
    }

    public String getUpdateTimeRange() {
        return updateTimeRange;
    }

    public void setUpdateTimeRange(String updateTimeRange) {
        this.updateTimeRange = updateTimeRange;
    }

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

    public String getConfigComment() {
        return configComment;
    }

    public void setConfigComment(String configComment) {
        this.configComment = configComment;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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
        if (this.getConfigComment() != null) {
            map.put("config_comment", this.getConfigComment());
        }
        if (this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (this.getUpdateTime() != null) {
            map.put("update_time", this.getUpdateTime());
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
        bean.setConfigComment(rs.getString("config_comment"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setUpdateTime(rs.getTimestamp("update_time"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbConfig beanFromRequest(HttpServletRequest request) {
        TbConfig bean = new TbConfig();
        bean.setConfigId(getLong("configId", request));
        bean.setConfigKey(getString("configKey", request));
        bean.setConfigValue(getString("configValue", request));
        bean.setConfigComment(getString("configComment", request));
        bean.setCreateTime(getTimestamp("createTime", request));
        bean.setUpdateTime(getTimestamp("updateTime", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        bean.setCreateTimeRange(getString("createTimeRange", request));
        bean.setUpdateTimeRange(getString("updateTimeRange", request));
        return bean;
    }
}