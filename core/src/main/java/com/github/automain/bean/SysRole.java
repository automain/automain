package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysRole implements BaseBean<SysRole> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 角色名称
    private String roleName;
    // 角色标识
    private String roleLabel;

    public Integer getId() {
        return id;
    }

    public SysRole setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysRole setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysRole setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public SysRole setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public SysRole setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_role";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(5);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
        }
        if (all || this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (all || this.getUpdateTime() != null) {
            map.put("update_time", this.getUpdateTime());
        }
        if (all || this.getRoleName() != null) {
            map.put("role_name", this.getRoleName());
        }
        if (all || this.getRoleLabel() != null) {
            map.put("role_label", this.getRoleLabel());
        }
        return map;
    }

    @Override
    public SysRole beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysRole()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setRoleName(rs.getString("role_name"))
                .setRoleLabel(rs.getString("role_label"));
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", roleName='" + roleName + '\'' +
                ", roleLabel='" + roleLabel + '\'' +
                '}';
    }
}