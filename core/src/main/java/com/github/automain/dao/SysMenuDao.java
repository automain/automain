package com.github.automain.dao;

import com.github.automain.bean.SysMenu;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.SysMenuVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysMenuDao extends BaseDao {

    private static final SysMenu DEFAULT_BEAN = new SysMenu();

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysMenu> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysMenu> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysMenu> selectTableForCustomPage(SysMenuVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysMenu> pageParamBean = new PageParamBean<SysMenu>()
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(SysMenuVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM sys_menu WHERE 1 = 1");
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

    public static List<IdNameVO> allValidMenu() throws SQLException {
        return executeSelectReturnList("SELECT sm.id, sm.menu_name AS 'name' FROM sys_menu sm", null, new IdNameVO());
    }

    public static List<SysMenu> selectAuthorityMenu(String userGid) throws SQLException {
        return executeSelectReturnList("SELECT sm.* FROM sys_user_role sur INNER JOIN sys_role_menu srm ON sur.role_id = srm.role_id INNER JOIN sys_menu sm ON srm.menu_id = sm.id WHERE sur.is_valid = 1 AND sur.user_gid = ? AND srm.is_valid = 1", List.of(userGid), new SysMenu());
    }
}