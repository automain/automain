package com.github.automain.monitor.vo;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DbSqlVO extends RequestUtil implements BaseBean<DbSqlVO> {

    // 删除数
    private Long comDelete;

    // 添加数
    private Long comInsert;

    // 查询数
    private Long comSelect;

    // 编辑数
    private Long comUpdate;

    // 创建时间
    private Timestamp createTime;

    // 连接池名称
    private String poolName;


    // ========== additional column begin ==========

    // 创建时间
    private String createTimeRange;

    public String getCreateTimeRange() {
        return createTimeRange;
    }

    public void setCreateTimeRange(String createTimeRange) {
        this.createTimeRange = createTimeRange;
    }

    // ========== additional column end ==========

    public Long getComDelete() {
        return comDelete;
    }

    public void setComDelete(Long comDelete) {
        this.comDelete = comDelete;
    }

    public Long getComInsert() {
        return comInsert;
    }

    public void setComInsert(Long comInsert) {
        this.comInsert = comInsert;
    }

    public Long getComSelect() {
        return comSelect;
    }

    public void setComSelect(Long comSelect) {
        this.comSelect = comSelect;
    }

    public Long getComUpdate() {
        return comUpdate;
    }

    public void setComUpdate(Long comUpdate) {
        this.comUpdate = comUpdate;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public String tableName() {
        return "db_status";
    }

    @Override
    public String primaryKey() {
        return "status_id";
    }

    @Override
    public Long primaryValue() {
        return 0L;
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getComDelete() != null) {
            map.put("com_delete", this.getComDelete());
        }
        if (this.getComInsert() != null) {
            map.put("com_insert", this.getComInsert());
        }
        if (this.getComSelect() != null) {
            map.put("com_select", this.getComSelect());
        }
        if (this.getComUpdate() != null) {
            map.put("com_update", this.getComUpdate());
        }
        if (this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (this.getPoolName() != null) {
            map.put("pool_name", this.getPoolName());
        }
        return map;
    }

    @Override
    public DbSqlVO beanFromResultSet(ResultSet rs) throws SQLException {
        DbSqlVO bean = new DbSqlVO();
        bean.setComDelete(rs.getLong("com_delete"));
        bean.setComInsert(rs.getLong("com_insert"));
        bean.setComSelect(rs.getLong("com_select"));
        bean.setComUpdate(rs.getLong("com_update"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPoolName(rs.getString("pool_name"));
        return bean;
    }

    @Override
    public DbSqlVO beanFromRequest(HttpServletRequest request) {
        DbSqlVO bean = new DbSqlVO();
        bean.setComDelete(getLong("comDelete", request));
        bean.setComInsert(getLong("comInsert", request));
        bean.setComSelect(getLong("comSelect", request));
        bean.setComUpdate(getLong("comUpdate", request));
        bean.setCreateTime(getTimestamp("createTime", request));
        bean.setCreateTimeRange(getString("createTimeRange", request));
        bean.setPoolName(getString("poolName", request));
        return bean;
    }
}
