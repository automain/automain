package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysRolePrivilege implements BaseBean<SysRolePrivilege> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // sys_role表主键
    private Integer roleId;
    // sys_privilege表主键
    private Integer privilegeId;

    public Integer getId() {
        return id;
    }

    public SysRolePrivilege setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysRolePrivilege setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysRolePrivilege setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysRolePrivilege setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysRolePrivilege setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public SysRolePrivilege setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_role_privilege";
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
        if (all || this.getRoleId() != null) {
            map.put("role_id", this.getRoleId());
        }
        if (all || this.getPrivilegeId() != null) {
            map.put("privilege_id", this.getPrivilegeId());
        }
        return map;
    }

    @Override
    public SysRolePrivilege beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysRolePrivilege()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setRoleId(rs.getInt("role_id"))
                .setPrivilegeId(rs.getInt("privilege_id"));
    }

    @Override
    public String toString() {
        return "SysRolePrivilege{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", roleId=" + roleId +
                ", privilegeId=" + privilegeId +
                '}';
    }
}