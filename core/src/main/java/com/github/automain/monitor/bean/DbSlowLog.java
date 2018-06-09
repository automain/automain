package com.github.automain.monitor.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DbSlowLog extends RequestUtil implements BaseBean<DbSlowLog> {

    // 慢查询ID
    private Long slowId;

    // 创建时间
    private Timestamp createTime;

    // 连接池名称
    private String poolName;

    // 慢查询库
    private String slowDb;

    // 慢查询用时
    private Integer slowTime;

    // 慢查询状态
    private String slowState;

    // 慢查询sql
    private String slowSql;

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

    public Long getSlowId() {
        return slowId;
    }

    public void setSlowId(Long slowId) {
        this.slowId = slowId;
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

    public String getSlowDb() {
        return slowDb;
    }

    public void setSlowDb(String slowDb) {
        this.slowDb = slowDb;
    }

    public Integer getSlowTime() {
        return slowTime;
    }

    public void setSlowTime(Integer slowTime) {
        this.slowTime = slowTime;
    }

    public String getSlowState() {
        return slowState;
    }

    public void setSlowState(String slowState) {
        this.slowState = slowState;
    }

    public String getSlowSql() {
        return slowSql;
    }

    public void setSlowSql(String slowSql) {
        this.slowSql = slowSql;
    }

    @Override
    public String tableName() {
        return "db_slow_log";
    }

    @Override
    public String primaryKey() {
        return "slow_id";
    }

    @Override
    public Long primaryValue() {
        return this.getSlowId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (all || this.getPoolName() != null) {
            map.put("pool_name", this.getPoolName());
        }
        if (all || this.getSlowDb() != null) {
            map.put("slow_db", this.getSlowDb());
        }
        if (all || this.getSlowTime() != null) {
            map.put("slow_time", this.getSlowTime());
        }
        if (all || this.getSlowState() != null) {
            map.put("slow_state", this.getSlowState());
        }
        if (all || this.getSlowSql() != null) {
            map.put("slow_sql", this.getSlowSql());
        }
        return map;
    }

    @Override
    public DbSlowLog beanFromResultSet(ResultSet rs) throws SQLException {
        DbSlowLog bean = new DbSlowLog();
        bean.setSlowId(rs.getLong("slow_id"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPoolName(rs.getString("pool_name"));
        bean.setSlowDb(rs.getString("slow_db"));
        bean.setSlowTime(rs.getInt("slow_time"));
        bean.setSlowState(rs.getString("slow_state"));
        bean.setSlowSql(rs.getString("slow_sql"));
        return bean;
    }

    @Override
    public DbSlowLog beanFromRequest(HttpServletRequest request) {
        DbSlowLog bean = new DbSlowLog();
        bean.setSlowId(getLong("slowId", request));
        bean.setCreateTime(getTimestamp("createTime", request));
        bean.setPoolName(getString("poolName", request));
        bean.setSlowDb(getString("slowDb", request));
        bean.setSlowTime(getInt("slowTime", request));
        bean.setSlowState(getString("slowState", request));
        bean.setSlowSql(getString("slowSql", request));
        bean.setCreateTimeRange(getString("createTimeRange", request));
        return bean;
    }
}