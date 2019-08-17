package com.github.automain.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbPrivilege extends RequestUtil implements BaseBean<TbPrivilege>, Comparable {

    // 权限ID
    private Long privilegeId;

    // 权限标识
    private String privilegeLabel;

    // 权限名称
    private String privilegeName;

    // 父级ID
    private Long parentId;

    // 顶级ID
    private Long topId;

    // 是否是最后一级(0:否,1;是)
    private Integer isLeaf;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeLabel() {
        return privilegeLabel;
    }

    public void setPrivilegeLabel(String privilegeLabel) {
        this.privilegeLabel = privilegeLabel;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_privilege";
    }

    @Override
    public String primaryKey() {
        return "privilege_id";
    }

    @Override
    public Long primaryValue() {
        return this.getPrivilegeId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getPrivilegeLabel() != null) {
            map.put("privilege_label", this.getPrivilegeLabel());
        }
        if (all || this.getPrivilegeName() != null) {
            map.put("privilege_name", this.getPrivilegeName());
        }
        if (all || this.getParentId() != null) {
            map.put("parent_id", this.getParentId());
        }
        if (all || this.getTopId() != null) {
            map.put("top_id", this.getTopId());
        }
        if (all || this.getIsLeaf() != null) {
            map.put("is_leaf", this.getIsLeaf());
        }
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbPrivilege beanFromResultSet(ResultSet rs) throws SQLException {
        TbPrivilege bean = new TbPrivilege();
        bean.setPrivilegeId(rs.getLong("privilege_id"));
        bean.setPrivilegeLabel(rs.getString("privilege_label"));
        bean.setPrivilegeName(rs.getString("privilege_name"));
        bean.setParentId(rs.getLong("parent_id"));
        bean.setTopId(rs.getLong("top_id"));
        bean.setIsLeaf(rs.getInt("is_leaf"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbPrivilege beanFromRequest(HttpServletRequest request) {
        TbPrivilege bean = new TbPrivilege();
        bean.setPrivilegeId(getLong("privilegeId", request));
        bean.setPrivilegeLabel(getString("privilegeLabel", request));
        bean.setPrivilegeName(getString("privilegeName", request));
        bean.setParentId(getLong("parentId", request));
        bean.setTopId(getLong("topId", request));
        bean.setIsLeaf(getInt("isLeaf", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof TbPrivilege){
            TbPrivilege target = (TbPrivilege) o;
            if (this.getParentId().equals(target.getParentId())) {
                return this.getPrivilegeId().compareTo(target.getPrivilegeId());
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}