package com.github.automain.dao;

import com.github.automain.bean.SysPrivilege;
import com.github.automain.vo.SysPrivilegeVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.ConnectionPool;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SysPrivilegeDao extends BaseDao<SysPrivilege> {

    private static final SysPrivilege DEFAULT_BEAN = new SysPrivilege();

    public int insertIntoTable(Connection connection, SysPrivilege bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysPrivilege bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysPrivilege> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysPrivilege bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysPrivilege bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysPrivilege paramBean, SysPrivilege newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int deleteTableById(Connection connection, SysPrivilege bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysPrivilege bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysPrivilege selectTableById(Connection connection, SysPrivilege bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysPrivilege> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysPrivilege selectOneTableByBean(Connection connection, SysPrivilege bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysPrivilege> selectTableByBean(Connection connection, SysPrivilege bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysPrivilege> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysPrivilege> selectTableForPage(Connection connection, SysPrivilege bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysPrivilege> selectTableForCustomPage(Connection connection, SysPrivilegeVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysPrivilege> pageParamBean = new PageParamBean<SysPrivilege>()
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

    private String setSearchCondition(SysPrivilegeVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_privilege WHERE 1 = 1");
        if (StringUtils.isNotBlank(bean.getPrivilegeLabel())) {
            sql.append(" AND privilege_label = ?");
            paramList.add(bean.getPrivilegeLabel());
        }
        if (StringUtils.isNotBlank(bean.getPrivilegeName())) {
            sql.append(" AND privilege_name = ?");
            paramList.add(bean.getPrivilegeName());
        }
        if (bean.getParentId() != null) {
            sql.append(" AND parent_id = ?");
            paramList.add(bean.getParentId());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }

    public Set<String> selectUserPrivilege(Connection connection, String userGid) throws SQLException {
        String sql = "SELECT sp.privilege_label FROM sys_user_role sur INNER JOIN sys_role_privilege srp ON sur.role_id = srp.role_id INNER JOIN sys_privilege sp ON srp.privilege_id = sp.id WHERE sur.is_valid = 1 AND srp.is_valid = 1 AND sur.user_gid = ? GROUP BY sp.privilege_label";
        ResultSet rs = null;
        Set<String> result = new HashSet<String>();
        try {
            rs = executeSelectReturnResultSet(connection, sql, List.of(userGid));
            while (rs.next()) {
                result.add(rs.getString("privilege_label"));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return result;
    }
}