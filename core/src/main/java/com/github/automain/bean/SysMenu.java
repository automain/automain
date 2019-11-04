package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysMenu implements BaseBean<SysMenu>, Comparable<SysMenu> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // 菜单路径
    private String menuPath;
    // 菜单名称
    private String menuName;
    // 菜单图标
    private String menuIcon;
    // 父级ID
    private Integer parentId;
    // 菜单排序
    private Integer sequenceNumber;

    public Integer getId() {
        return id;
    }

    public SysMenu setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysMenu setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysMenu setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysMenu setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public SysMenu setMenuPath(String menuPath) {
        this.menuPath = menuPath;
        return this;
    }

    public String getMenuName() {
        return menuName;
    }

    public SysMenu setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public SysMenu setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }

    public SysMenu setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public SysMenu setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_menu";
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
        if (all || this.getMenuPath() != null) {
            map.put("menu_path", this.getMenuPath());
        }
        if (all || this.getMenuName() != null) {
            map.put("menu_name", this.getMenuName());
        }
        if (all || this.getMenuIcon() != null) {
            map.put("menu_icon", this.getMenuIcon());
        }
        if (all || this.getParentId() != null) {
            map.put("parent_id", this.getParentId());
        }
        if (all || this.getSequenceNumber() != null) {
            map.put("sequence_number", this.getSequenceNumber());
        }
        return map;
    }

    @Override
    public SysMenu beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysMenu()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setMenuPath(rs.getString("menu_path"))
                .setMenuName(rs.getString("menu_name"))
                .setMenuIcon(rs.getString("menu_icon"))
                .setParentId(rs.getInt("parent_id"))
                .setSequenceNumber(rs.getInt("sequence_number"));
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", menuPath='" + menuPath + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", parentId=" + parentId +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }

    @Override
    public int compareTo(SysMenu o) {
        if (o == null || o.getSequenceNumber() == null || this.getSequenceNumber() == null) {
            return 0;
        }
        return this.getSequenceNumber().compareTo(o.getSequenceNumber());
    }
}