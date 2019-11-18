package com.github.automain.dao;

import com.github.automain.bean.SysUserRole;
import com.github.fastjdbc.BaseDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SysUserRoleDao extends BaseDao {

    private static final SysUserRole DEFAULT_BEAN = new SysUserRole();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysUserRole> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysUserRole> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }
}