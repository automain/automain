package com.github.automain.monitor.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DbStatus extends RequestUtil implements BaseBean<DbStatus> {

    // 提交数
    private Long comCommit;

    // 删除数
    private Long comDelete;

    // 添加数
    private Long comInsert;

    // 回滚数
    private Long comRollback;

    // 查询数
    private Long comSelect;

    // 编辑数
    private Long comUpdate;

    // 创建时间
    private Timestamp createTime;

    // 已用页大小
    private Long pagesData;

    // 空闲页大小
    private Long pagesFree;

    // 忙碌页大小
    private Long pagesMisc;

    // 连接池名称
    private String poolName;

    // 主键
    private Long statusId;

    // 空闲线程数
    private Long threadsFree;

    // 运行中线程数
    private Long threadsRunning;

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

    public Long getComCommit() {
        return comCommit;
    }

    public void setComCommit(Long comCommit) {
        this.comCommit = comCommit;
    }

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

    public Long getComRollback() {
        return comRollback;
    }

    public void setComRollback(Long comRollback) {
        this.comRollback = comRollback;
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

    public Long getPagesData() {
        return pagesData;
    }

    public void setPagesData(Long pagesData) {
        this.pagesData = pagesData;
    }

    public Long getPagesFree() {
        return pagesFree;
    }

    public void setPagesFree(Long pagesFree) {
        this.pagesFree = pagesFree;
    }

    public Long getPagesMisc() {
        return pagesMisc;
    }

    public void setPagesMisc(Long pagesMisc) {
        this.pagesMisc = pagesMisc;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

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
        return this.getStatusId();
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getComCommit() != null) {
            map.put("com_commit", this.getComCommit());
        }
        if (this.getComDelete() != null) {
            map.put("com_delete", this.getComDelete());
        }
        if (this.getComInsert() != null) {
            map.put("com_insert", this.getComInsert());
        }
        if (this.getComRollback() != null) {
            map.put("com_rollback", this.getComRollback());
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
        if (this.getPagesData() != null) {
            map.put("pages_data", this.getPagesData());
        }
        if (this.getPagesFree() != null) {
            map.put("pages_free", this.getPagesFree());
        }
        if (this.getPagesMisc() != null) {
            map.put("pages_misc", this.getPagesMisc());
        }
        if (this.getPoolName() != null) {
            map.put("pool_name", this.getPoolName());
        }
        if (this.getThreadsFree() != null) {
            map.put("threads_free", this.getThreadsFree());
        }
        if (this.getThreadsRunning() != null) {
            map.put("threads_running", this.getThreadsRunning());
        }
        return map;
    }

    @Override
    public DbStatus beanFromResultSet(ResultSet rs) throws SQLException {
        DbStatus bean = new DbStatus();
        bean.setComCommit(rs.getLong("com_commit"));
        bean.setComDelete(rs.getLong("com_delete"));
        bean.setComInsert(rs.getLong("com_insert"));
        bean.setComRollback(rs.getLong("com_rollback"));
        bean.setComSelect(rs.getLong("com_select"));
        bean.setComUpdate(rs.getLong("com_update"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPagesData(rs.getLong("pages_data"));
        bean.setPagesFree(rs.getLong("pages_free"));
        bean.setPagesMisc(rs.getLong("pages_misc"));
        bean.setPoolName(rs.getString("pool_name"));
        bean.setStatusId(rs.getLong("status_id"));
        bean.setThreadsFree(rs.getLong("threads_free"));
        bean.setThreadsRunning(rs.getLong("threads_running"));
        return bean;
    }

    @Override
    public DbStatus beanFromRequest(HttpServletRequest request) {
        DbStatus bean = new DbStatus();
        bean.setComCommit(getLong("comCommit", request));
        bean.setComDelete(getLong("comDelete", request));
        bean.setComInsert(getLong("comInsert", request));
        bean.setComRollback(getLong("comRollback", request));
        bean.setComSelect(getLong("comSelect", request));
        bean.setComUpdate(getLong("comUpdate", request));
        bean.setCreateTime(getTimestamp("createTime", request));
        bean.setPagesData(getLong("pagesData", request));
        bean.setPagesFree(getLong("pagesFree", request));
        bean.setPagesMisc(getLong("pagesMisc", request));
        bean.setPoolName(getString("poolName", request));
        bean.setStatusId(getLong("statusId", request));
        bean.setThreadsFree(getLong("threadsFree", request));
        bean.setThreadsRunning(getLong("threadsRunning", request));
        bean.setCreateTimeRange(getString("createTimeRange", request));
        return bean;
    }
}