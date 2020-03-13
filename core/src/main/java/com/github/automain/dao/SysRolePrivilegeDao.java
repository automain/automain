package com.github.automain.dao;

import com.github.automain.bean.SysRolePrivilege;
import com.github.fastjdbc.BaseDao;

import java.sql.SQLException;
import java.util.List;

public class SysRolePrivilegeDao extends BaseDao {

    private static final SysRolePrivilege DEFAULT_BEAN = new SysRolePrivilege();

    public static int softDeleteTableByIdList(List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysRolePrivilege> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysRolePrivilege> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    public static List<Integer> selectCheckedPrivilegeList(Integer roleId) throws SQLException {
        return executeSelectReturnIntegerList("SELECT srp.privilege_id FROM sys_role_privilege srp WHERE srp.is_valid = 1 AND srp.role_id = ?", List.of(roleId));
    }

    public static int softDeleteByRoleId(Integer roleId) throws SQLException {
        return executeUpdate("UPDATE sys_role_privilege srp SET srp.is_valid = 0 WHERE srp.role_id = ?", List.of(roleId));
    }
}