package com.github.automain.dao;

import com.github.automain.bean.SysUser;
import com.github.automain.vo.SysUserVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysUserDao extends BaseDao<SysUser> {

    private static final SysUser DEFAULT_BEAN = new SysUser();

    public int insertIntoTable(Connection connection, SysUser bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysUser bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysUser> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysUser bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByGid(Connection connection, SysUser bean, boolean all) throws SQLException {
        return super.updateTableByGid(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysUser bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTableByGidList(Connection connection, SysUser bean, List<String> gidList, boolean all) throws SQLException {
        return super.updateTableByGidList(connection, bean, gidList, all);
    }

    public int updateTable(Connection connection, SysUser paramBean, SysUser newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int softDeleteTableById(Connection connection, SysUser bean) throws SQLException {
        return super.softDeleteTableById(connection, bean);
    }

    public int softDeleteTableByGid(Connection connection, SysUser bean) throws SQLException {
        return super.softDeleteTableByGid(connection, bean);
    }

    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int softDeleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return super.softDeleteTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public int deleteTableById(Connection connection, SysUser bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByGid(Connection connection, SysUser bean) throws SQLException {
        return super.deleteTableByGid(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int deleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return super.deleteTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public int countTableByBean(Connection connection, SysUser bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysUser selectTableById(Connection connection, SysUser bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public SysUser selectTableByGid(Connection connection, SysUser bean) throws SQLException {
        return super.selectTableByGid(connection, bean);
    }

    public List<SysUser> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public List<SysUser> selectTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return super.selectTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public SysUser selectOneTableByBean(Connection connection, SysUser bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysUser> selectTableByBean(Connection connection, SysUser bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysUser> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysUser> selectTableForPage(Connection connection, SysUser bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysUser> selectTableForCustomPage(Connection connection, SysUserVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysUser> pageParamBean = new PageParamBean<SysUser>()
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

    private String setSearchCondition(SysUserVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_user WHERE is_valid = 1");
        if (StringUtils.isNotBlank(bean.getGid())) {
            sql.append(" AND gid = ?");
            paramList.add(bean.getGid());
        }
        if (StringUtils.isNotBlank(bean.getUserName())) {
            sql.append(" AND user_name = ?");
            paramList.add(bean.getUserName());
        }
        if (StringUtils.isNotBlank(bean.getPhone())) {
            sql.append(" AND phone = ?");
            paramList.add(bean.getPhone());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}