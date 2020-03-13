package com.github.automain.dao;

import com.github.automain.bean.SysRoleMenu;
import com.github.fastjdbc.BaseDao;

import java.sql.SQLException;
import java.util.List;

public class SysRoleMenuDao extends BaseDao {

    private static final SysRoleMenu DEFAULT_BEAN = new SysRoleMenu();

    public static int softDeleteTableByIdList(List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysRoleMenu> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysRoleMenu> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    public static List<Integer> selectCheckedMenuList(Integer roleId) throws SQLException {
        return executeSelectReturnIntegerList("SELECT srm.menu_id FROM sys_role_menu srm WHERE srm.is_valid = 1 AND srm.role_id = ?", List.of(roleId));
    }

    public static int softDeleteByRoleId(Integer roleId) throws SQLException {
        return executeUpdate("UPDATE sys_role_menu srm SET srm.is_valid = 0 WHERE srm.role_id = ?", List.of(roleId));
    }
}