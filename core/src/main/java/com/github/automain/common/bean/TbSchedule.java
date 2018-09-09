package com.github.automain.common.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TbSchedule extends RequestUtil implements BaseBean<TbSchedule> {

    // 任务ID
    private Long scheduleId;

    // 任务名称
    private String scheduleName;

    // 任务请求url
    private String scheduleUrl;

    // 开始执行时间
    private Timestamp startExecuteTime;

    // 间隔时间长度
    private Long delayTime;

    // 上次执行时间
    private Timestamp lastExecuteTime;

    // 修改时间
    private Timestamp updateTime;

    // 是否关闭(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========

    // 开始执行时间
    private String startExecuteTimeRange;
    // 上次执行时间
    private String lastExecuteTimeRange;
    // 修改时间
    private String updateTimeRange;

    public String getStartExecuteTimeRange() {
        return startExecuteTimeRange;
    }

    public void setStartExecuteTimeRange(String startExecuteTimeRange) {
        this.startExecuteTimeRange = startExecuteTimeRange;
    }

    public String getLastExecuteTimeRange() {
        return lastExecuteTimeRange;
    }

    public void setLastExecuteTimeRange(String lastExecuteTimeRange) {
        this.lastExecuteTimeRange = lastExecuteTimeRange;
    }

    public String getUpdateTimeRange() {
        return updateTimeRange;
    }

    public void setUpdateTimeRange(String updateTimeRange) {
        this.updateTimeRange = updateTimeRange;
    }

    // ========== additional column end ==========

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(String scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
    }

    public Timestamp getStartExecuteTime() {
        return startExecuteTime;
    }

    public void setStartExecuteTime(Timestamp startExecuteTime) {
        this.startExecuteTime = startExecuteTime;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Timestamp getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Timestamp lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
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
        return "tb_schedule";
    }

    @Override
    public String primaryKey() {
        return "schedule_id";
    }

    @Override
    public Long primaryValue() {
        return this.getScheduleId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getScheduleName() != null) {
            map.put("schedule_name", this.getScheduleName());
        }
        if (all || this.getScheduleUrl() != null) {
            map.put("schedule_url", this.getScheduleUrl());
        }
        if (all || this.getStartExecuteTime() != null) {
            map.put("start_execute_time", this.getStartExecuteTime());
        }
        if (all || this.getDelayTime() != null) {
            map.put("delay_time", this.getDelayTime());
        }
        if (all || this.getLastExecuteTime() != null) {
            map.put("last_execute_time", this.getLastExecuteTime());
        }
        if (all || this.getUpdateTime() != null) {
            map.put("update_time", this.getUpdateTime());
        }
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbSchedule beanFromResultSet(ResultSet rs) throws SQLException {
        TbSchedule bean = new TbSchedule();
        bean.setScheduleId(rs.getLong("schedule_id"));
        bean.setScheduleName(rs.getString("schedule_name"));
        bean.setScheduleUrl(rs.getString("schedule_url"));
        bean.setStartExecuteTime(rs.getTimestamp("start_execute_time"));
        bean.setDelayTime(rs.getLong("delay_time"));
        bean.setLastExecuteTime(rs.getTimestamp("last_execute_time"));
        bean.setUpdateTime(rs.getTimestamp("update_time"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbSchedule beanFromRequest(HttpServletRequest request) {
        TbSchedule bean = new TbSchedule();
        bean.setScheduleId(getLong("scheduleId", request));
        bean.setScheduleName(getString("scheduleName", request));
        bean.setScheduleUrl(getString("scheduleUrl", request));
        bean.setStartExecuteTime(getTimestamp("startExecuteTime", request));
        bean.setDelayTime(getLong("delayTime", request));
        bean.setLastExecuteTime(getTimestamp("lastExecuteTime", request));
        bean.setUpdateTime(getTimestamp("updateTime", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        bean.setStartExecuteTimeRange(getString("startExecuteTimeRange", request));
        bean.setLastExecuteTimeRange(getString("lastExecuteTimeRange", request));
        bean.setUpdateTimeRange(getString("updateTimeRange", request));
        return bean;
    }
}