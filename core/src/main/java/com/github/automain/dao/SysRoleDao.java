package com.github.automain.dao;

import com.github.automain.bean.SysRole;
import com.github.automain.vo.RoleVO;
import com.github.automain.vo.SysRoleVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.ConnectionPool;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysRoleDao extends BaseDao {

    private static final SysRole DEFAULT_BEAN = new SysRole();

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRole> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRole> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysRole> selectTableForCustomPage(Connection connection, SysRoleVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysRole> pageParamBean = new PageParamBean<SysRole>()
                .setConnection(connection)
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(SysRoleVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_role WHERE 1 = 1");
        if (StringUtils.isNotBlank(bean.getRoleName())) {
            sql.append(" AND role_name = ?");
            paramList.add(bean.getRoleName());
        }
        if (StringUtils.isNotBlank(bean.getRoleLabel())) {
            sql.append(" AND role_label = ?");
            paramList.add(bean.getRoleLabel());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }

    public static List<RoleVO> selectAllRoleVO(Connection connection) throws SQLException {
        String sql = "SELECT sr.role_name,sr.role_label FROM sys_role sr";
        return executeSelectReturnList(connection, sql, null, new RoleVO());
    }

    public static List<Integer> selectRoleIdByLabelList(Connection connection, List<String> labelList) throws SQLException {
        String sql = "SELECT sr.id FROM sys_role sr WHERE sr.role_label" + makeInStr(labelList);
        ResultSet rs = null;
        List<Integer> idList = new ArrayList<Integer>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, labelList);
            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return idList;
    }

    public static boolean checkRoleLabelUseable(Connection connection, String roleLabel, Integer id) throws SQLException {
        String sql = "SELECT sr.id FROM sys_role sr WHERE sr.role_label = ? LIMIT 1";
        ResultSet rs = null;
        try {
            rs = executeSelectReturnResultSet(connection, sql, List.of(roleLabel));
            if (rs.next()) {
                return Integer.valueOf(rs.getInt("id")).equals(id);
            } else {
                return true;
            }
        } finally {
            ConnectionPool.close(rs);
        }
    }
}