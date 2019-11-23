package com.github.automain.dao;

import com.github.automain.bean.SysRoleMenu;
import com.github.fastjdbc.BaseDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SysRoleMenuDao extends BaseDao {

    private static final SysRoleMenu DEFAULT_BEAN = new SysRoleMenu();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRoleMenu> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysRoleMenu> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    public static List<Integer> selectCheckedMenuList(Connection connection, Integer roleId) throws SQLException {
        return executeSelectReturnIntegerList(connection, "SELECT srm.menu_id FROM sys_role_menu srm WHERE srm.is_valid = 1 AND srm.role_id = ?", List.of(roleId));
    }

    public static int softDeleteByRoleId(Connection connection, Integer roleId) throws SQLException {
        return executeUpdate(connection, "UPDATE sys_role_menu srm SET srm.is_valid = 0 WHERE srm.role_id = ?", List.of(roleId));
    }
}