package com.github.automain.dao;

import com.github.automain.bean.SysUserRole;
import com.github.fastjdbc.BaseDao;

import java.sql.SQLException;
import java.util.List;

public class SysUserRoleDao extends BaseDao {

    private static final SysUserRole DEFAULT_BEAN = new SysUserRole();

    public static int softDeleteTableByIdList(List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysUserRole> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysUserRole> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    public static int softDeleteRoleByUserGid(String userGid) throws SQLException {
        return executeUpdate("UPDATE sys_user_role sur SET sur.is_valid = 0 WHERE sur.user_gid = ?", List.of(userGid));
    }

    public static List<String> selectUserRoleLabelList(String userGid) throws SQLException {
        return executeSelectReturnStringList("SELECT sr.role_label FROM sys_user_role sur INNER JOIN sys_role sr ON sur.role_id = sr.id WHERE sur.is_valid = 1 AND sur.user_gid = ?", List.of(userGid));
    }
}