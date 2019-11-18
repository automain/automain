package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysSchedule implements BaseBean<SysSchedule> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // 任务名称
    private String scheduleName;
    // 任务地址
    private String scheduleUrl;
    // 开始执行时间
    private Integer startExecuteTime;
    // 间隔时间(秒)
    private Integer delayTime;
    // 上次执行时间
    private Integer lastExecuteTime;

    public Integer getId() {
        return id;
    }

    public SysSchedule setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysSchedule setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysSchedule setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysSchedule setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public SysSchedule setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public SysSchedule setScheduleUrl(String scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
        return this;
    }

    public Integer getStartExecuteTime() {
        return startExecuteTime;
    }

    public SysSchedule setStartExecuteTime(Integer startExecuteTime) {
        this.startExecuteTime = startExecuteTime;
        return this;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public SysSchedule setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public Integer getLastExecuteTime() {
        return lastExecuteTime;
    }

    public SysSchedule setLastExecuteTime(Integer lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_schedule";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(9);
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
        return map;
    }

    @Override
    public SysSchedule beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysSchedule()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setScheduleName(rs.getString("schedule_name"))
                .setScheduleUrl(rs.getString("schedule_url"))
                .setStartExecuteTime(rs.getInt("start_execute_time"))
                .setDelayTime(rs.getInt("delay_time"))
                .setLastExecuteTime(rs.getInt("last_execute_time"));
    }

    @Override
    public String toString() {
        return "SysSchedule{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", scheduleName='" + scheduleName + '\'' +
                ", scheduleUrl='" + scheduleUrl + '\'' +
                ", startExecuteTime=" + startExecuteTime +
                ", delayTime=" + delayTime +
                ", lastExecuteTime=" + lastExecuteTime +
                '}';
    }
}