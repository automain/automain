package com.github.automain.dao;

import com.github.automain.bean.SysRolePrivilege;
import com.github.automain.vo.SysRolePrivilegeVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysRolePrivilegeDao extends BaseDao<SysRolePrivilege> {

    private static final SysRolePrivilege DEFAULT_BEAN = new SysRolePrivilege();

    public int insertIntoTable(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysRolePrivilege> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysRolePrivilege bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysRolePrivilege bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysRolePrivilege paramBean, SysRolePrivilege newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int softDeleteTableById(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.softDeleteTableById(connection, bean);
    }

    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int deleteTableById(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysRolePrivilege selectTableById(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysRolePrivilege> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysRolePrivilege selectOneTableByBean(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysRolePrivilege> selectTableByBean(Connection connection, SysRolePrivilege bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysRolePrivilege> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysRolePrivilege> selectTableForPage(Connection connection, SysRolePrivilege bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysRolePrivilege> selectTableForCustomPage(Connection connection, SysRolePrivilegeVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysRolePrivilege> pageParamBean = new PageParamBean<SysRolePrivilege>()
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

    private String setSearchCondition(SysRolePrivilegeVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_role_privilege WHERE is_valid = 1");
        if (bean.getRoleId() != null) {
            sql.append(" AND role_id = ?");
            paramList.add(bean.getRoleId());
        }
        if (bean.getPrivilegeId() != null) {
            sql.append(" AND privilege_id = ?");
            paramList.add(bean.getPrivilegeId());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}