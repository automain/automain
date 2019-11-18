package com.github.automain.dao;

import com.github.automain.bean.SysRole;
import com.github.automain.vo.RoleVO;
import com.github.automain.vo.SysRoleVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysRoleDao extends BaseDao<SysRole> {

    private static final SysRole DEFAULT_BEAN = new SysRole();

    public int insertIntoTable(Connection connection, SysRole bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysRole bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysRole> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysRole bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysRole bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysRole paramBean, SysRole newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int deleteTableById(Connection connection, SysRole bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysRole bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysRole selectTableById(Connection connection, SysRole bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysRole> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysRole selectOneTableByBean(Connection connection, SysRole bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysRole> selectTableByBean(Connection connection, SysRole bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysRole> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysRole> selectTableForPage(Connection connection, SysRole bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysRole> selectTableForCustomPage(Connection connection, SysRoleVO bean) throws Exception {
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

    private String setSearchCondition(SysRoleVO bean, List<Object> paramList, boolean isCountSql) {
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

    public List<RoleVO> selectAllRoleVO(Connection connection) throws SQLException {
        String sql = "SELECT sr.role_name,sr.role_label FROM sys_role sr";
        return executeSelectReturnList(connection, sql, null, new RoleVO());
    }
}