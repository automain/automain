package com.github.automain.dao;

import com.github.automain.bean.SysRoleMenu;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SysRoleMenuDao extends BaseDao<SysRoleMenu> {

    private static final SysRoleMenu DEFAULT_BEAN = new SysRoleMenu();

    public int insertIntoTable(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysRoleMenu> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysRoleMenu bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysRoleMenu bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysRoleMenu paramBean, SysRoleMenu newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int softDeleteTableById(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.softDeleteTableById(connection, bean);
    }

    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int deleteTableById(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysRoleMenu selectTableById(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysRoleMenu> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysRoleMenu selectOneTableByBean(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysRoleMenu> selectTableByBean(Connection connection, SysRoleMenu bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysRoleMenu> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysRoleMenu> selectTableForPage(Connection connection, SysRoleMenu bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }
}