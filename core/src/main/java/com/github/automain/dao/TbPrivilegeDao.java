package com.github.automain.dao;

import com.github.automain.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TbPrivilegeDao extends BaseDao<TbPrivilege> {

    @SuppressWarnings("unchecked")
    public PageBean<TbPrivilege> selectTableForCustomPage(ConnectionBean connection, TbPrivilege bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbPrivilege> pageParameterBean = new PageParameterBean<TbPrivilege>();
        pageParameterBean.setConnection(connection);
        pageParameterBean.setBean(bean);
        pageParameterBean.setCountSql(countSql);
        pageParameterBean.setCountParameterList(countParameterList);
        pageParameterBean.setSql(sql);
        pageParameterBean.setParameterList(parameterList);
        pageParameterBean.setPage(page);
        pageParameterBean.setLimit(limit);
        return selectTableForPage(pageParameterBean);
    }

    private String setSearchCondition(TbPrivilege bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_privilege WHERE is_delete = 0 ");
        if (bean.getParentId() != null) {
            sql.append(" AND parent_id = ?");
            parameterList.add(bean.getParentId());
        }
        if (bean.getPrivilegeName() != null) {
            sql.append(" AND privilege_name LIKE ?");
            parameterList.add(bean.getPrivilegeName() + "%");
        }
        if (bean.getPrivilegeLabel() != null) {
            sql.append(" AND privilege_label = ?");
            parameterList.add(bean.getPrivilegeLabel());
        }
        if (bean.getTopId() != null) {
            sql.append(" AND top_id = ?");
            parameterList.add(bean.getTopId());
        }
        if (bean.getIsLeaf() != null) {
            sql.append(" AND is_leaf = ?");
            parameterList.add(bean.getIsLeaf());
        }
        return sql.toString();
    }

    public List<Long> selectPrivilegeIdByParentId(ConnectionBean connection, Long parentId) throws SQLException {
        String sql = "SELECT privilege_id FROM tb_privilege WHERE parent_id = ? AND is_delete = 0";
        ResultSet rs = null;
        List<Long> privilegeIdList = new ArrayList<Long>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, Collections.singletonList(parentId));
            while (rs.next()) {
                privilegeIdList.add(rs.getLong(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return privilegeIdList;
    }

}