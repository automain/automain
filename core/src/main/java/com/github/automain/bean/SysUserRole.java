package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysUserRole implements BaseBean<SysUserRole> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // sys_user表gid
    private String userGid;
    // sys_role表主键
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public SysUserRole setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysUserRole setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysUserRole setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysUserRole setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getUserGid() {
        return userGid;
    }

    public SysUserRole setUserGid(String userGid) {
        this.userGid = userGid;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysUserRole setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_user_role";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(6);
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
        if (all || this.getUserGid() != null) {
            map.put("user_gid", this.getUserGid());
        }
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        return map;
    }

    @Override
    public SysUserRole beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysUserRole()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setUserGid(rs.getString("user_gid"))
                .setRoleId(rs.getInt("role_id"));
    }

    @Override
    public String toString() {
        return "SysUserRole{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", userGid=" + userGid +
                ", roleId=" + roleId +
                '}';
    }
}