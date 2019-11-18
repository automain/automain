package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysPrivilege implements BaseBean<SysPrivilege> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 权限标识
    private String privilegeLabel;
    // 权限名称
    private String privilegeName;
    // 父级ID
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public SysPrivilege setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysPrivilege setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysPrivilege setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getPrivilegeLabel() {
        return privilegeLabel;
    }

    public SysPrivilege setPrivilegeLabel(String privilegeLabel) {
        this.privilegeLabel = privilegeLabel;
        return this;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public SysPrivilege setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }

    public SysPrivilege setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_privilege";
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
        if (all || this.getPrivilegeLabel() != null) {
            map.put("privilege_label", this.getPrivilegeLabel());
        }
        if (all || this.getPrivilegeName() != null) {
            map.put("privilege_name", this.getPrivilegeName());
        }
        if (all || this.getParentId() != null) {
            map.put("parent_id", this.getParentId());
        }
        return map;
    }

    @Override
    public SysPrivilege beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysPrivilege()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setPrivilegeLabel(rs.getString("privilege_label"))
                .setPrivilegeName(rs.getString("privilege_name"))
                .setParentId(rs.getInt("parent_id"));
    }

    @Override
    public String toString() {
        return "SysPrivilege{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", privilegeLabel='" + privilegeLabel + '\'' +
                ", privilegeName='" + privilegeName + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}