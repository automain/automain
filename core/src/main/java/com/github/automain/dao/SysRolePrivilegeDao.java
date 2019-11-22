package com.github.automain.dao;

import com.github.automain.bean.SysRolePrivilege;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysRolePrivilegeDao extends BaseDao {

    private static final SysRolePrivilege DEFAULT_BEAN = new SysRolePrivilege();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRolePrivilege> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRolePrivilege> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    public static List<Integer> selectCheckedPrivilegeList(Connection connection, Integer roleId) throws SQLException {
        String sql = "SELECT srp.privilege_id FROM sys_role_privilege srp WHERE srp.is_valid = 1 AND srp.role_id = ?";
        ResultSet rs = null;
        List<Integer> result = new ArrayList<Integer>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, List.of(roleId));
            while (rs.next()) {
                result.add(rs.getInt("privilege_id"));
            }
            return result;
        } finally {
            ConnectionPool.close(rs);
        }
    }

    public static int softDeleteByRoleId(Connection connection, Integer roleId) throws SQLException {
        String sql = "UPDATE sys_role_privilege srp SET srp.is_valid = 0 WHERE srp.role_id = ?";
        return executeUpdate(connection, sql, List.of(roleId));
    }
}