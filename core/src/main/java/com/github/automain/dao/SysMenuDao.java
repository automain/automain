package com.github.automain.dao;

import com.github.automain.bean.SysMenu;
import com.github.automain.vo.SysMenuVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysMenuDao extends BaseDao<SysMenu> {

    private static final SysMenu DEFAULT_BEAN = new SysMenu();

    public int insertIntoTable(Connection connection, SysMenu bean) throws SQLException {
        return super.insertIntoTable(connection, bean);
    }

    public Integer insertIntoTableReturnId(Connection connection, SysMenu bean) throws SQLException {
        return super.insertIntoTableReturnId(connection, bean);
    }

    public int batchInsertIntoTable(Connection connection, List<SysMenu> list) throws SQLException {
        return super.batchInsertIntoTable(connection, list);
    }

    public int updateTableById(Connection connection, SysMenu bean, boolean all) throws SQLException {
        return super.updateTableById(connection, bean, all);
    }

    public int updateTableByIdList(Connection connection, SysMenu bean, List<Integer> idList, boolean all) throws SQLException {
        return super.updateTableByIdList(connection, bean, idList, all);
    }

    public int updateTable(Connection connection, SysMenu paramBean, SysMenu newBean, boolean insertWhenNotExist, boolean updateMulti, boolean all) throws SQLException {
        return super.updateTable(connection, paramBean, newBean, insertWhenNotExist, updateMulti, all);
    }

    public int softDeleteTableById(Connection connection, SysMenu bean) throws SQLException {
        return super.softDeleteTableById(connection, bean);
    }

    public int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int deleteTableById(Connection connection, SysMenu bean) throws SQLException {
        return super.deleteTableById(connection, bean);
    }

    public int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public int countTableByBean(Connection connection, SysMenu bean) throws SQLException {
        return super.countTableByBean(connection, bean);
    }

    public SysMenu selectTableById(Connection connection, SysMenu bean) throws SQLException {
        return super.selectTableById(connection, bean);
    }

    public List<SysMenu> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return super.selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public SysMenu selectOneTableByBean(Connection connection, SysMenu bean) throws SQLException {
        return super.selectOneTableByBean(connection, bean);
    }

    public List<SysMenu> selectTableByBean(Connection connection, SysMenu bean) throws SQLException {
        return super.selectTableByBean(connection, bean);
    }

    public List<SysMenu> selectAllTable(Connection connection) throws SQLException {
        return super.selectAllTable(connection, DEFAULT_BEAN);
    }

    public PageBean<SysMenu> selectTableForPage(Connection connection, SysMenu bean, int page, int size) throws Exception {
        return super.selectTableForPage(connection, bean, page, size);
    }

    @SuppressWarnings("unchecked")
    public PageBean<SysMenu> selectTableForCustomPage(Connection connection, SysMenuVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysMenu> pageParamBean = new PageParamBean<SysMenu>()
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

    private String setSearchCondition(SysMenuVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_menu WHERE is_valid = 1");
        if (StringUtils.isNotBlank(bean.getMenuName())) {
            sql.append(" AND menu_name LIKE ?");
            paramList.add(bean.getMenuName() + "%");
        }
        if (bean.getParentId() != null) {
            sql.append(" AND parent_id = ?");
            paramList.add(bean.getParentId());
        }
        if (bean.getSequenceNumber() != null) {
            sql.append(" AND sequence_number = ?");
            paramList.add(bean.getSequenceNumber());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY ").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}