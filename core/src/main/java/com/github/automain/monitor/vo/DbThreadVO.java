package com.github.automain.monitor.vo;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class DbThreadVO extends RequestUtil implements BaseBean<DbThreadVO> {

    // 空闲线程数
    private Long threadsFree;

    // 运行中线程数
    private Long threadsRunning;

    // 创建时间
    private Timestamp createTime;

    // 连接池名称
    private String poolName;

    public Long getThreadsFree() {
        return threadsFree;
    }

    public void setThreadsFree(Long threadsFree) {
        this.threadsFree = threadsFree;
    }

    public Long getThreadsRunning() {
        return threadsRunning;
    }

    public void setThreadsRunning(Long threadsRunning) {
        this.threadsRunning = threadsRunning;
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
        return null;
    }

    @Override
    public String primaryKey() {
        return null;
    }

    @Override
    public Long primaryValue() {
        return null;
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        return null;
    }

    @Override
    public DbThreadVO beanFromRequest(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public DbThreadVO beanFromResultSet(ResultSet rs) throws SQLException {
        DbThreadVO bean = new DbThreadVO();
        bean.setThreadsFree(rs.getLong("threads_free"));
        bean.setThreadsRunning(rs.getLong("threads_running"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPoolName(rs.getString("pool_name"));
        return bean;
    }
}
