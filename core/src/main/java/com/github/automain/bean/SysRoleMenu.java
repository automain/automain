package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysRoleMenu implements BaseBean<SysRoleMenu> {

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
    // sys_menu表主键
    private Integer menuId;

    public Integer getId() {
        return id;
    }

    public SysRoleMenu setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysRoleMenu setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysRoleMenu setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysRoleMenu setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysRoleMenu setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public SysRoleMenu setMenuId(Integer menuId) {
        this.menuId = menuId;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_role_menu";
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
        if (all || this.getMenuId() != null) {
            map.put("menu_id", this.getMenuId());
        }
        return map;
    }

    @Override
    public SysRoleMenu beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysRoleMenu()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setRoleId(rs.getInt("role_id"))
                .setMenuId(rs.getInt("menu_id"));
    }

    @Override
    public String toString() {
        return "SysRoleMenu{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }
}