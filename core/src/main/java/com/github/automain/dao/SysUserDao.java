package com.github.automain.dao;

import com.github.automain.bean.SysUser;
import com.github.automain.vo.SysUserVO;
import com.github.automain.vo.UserVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysUserDao extends BaseDao {

    private static final SysUser DEFAULT_BEAN = new SysUser();

    public static int softDeleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int softDeleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return softDeleteTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public static int deleteTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return deleteTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static int deleteTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return deleteTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public static List<SysUser> selectTableByIdList(Connection connection, List<Integer> idList) throws SQLException {
        return selectTableByIdList(connection, DEFAULT_BEAN, idList);
    }

    public static List<SysUser> selectTableByGidList(Connection connection, List<String> gidList) throws SQLException {
        return selectTableByGidList(connection, DEFAULT_BEAN, gidList);
    }

    public static List<SysUser> selectAllTable(Connection connection) throws SQLException {
        return selectAllTable(connection, DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<UserVO> selectTableForCustomPage(Connection connection, SysUserVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<UserVO> pageParamBean = new PageParamBean<UserVO>()
                .setConnection(connection)
                .setBean(new UserVO())
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(SysUserVO bean, List<Object> paramList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "su.gid,su.create_time,su.update_time,su.user_name,su.real_name,su.phone,su.email,GROUP_CONCAT(sr.role_name) AS 'role_name'")
                .append(" FROM sys_user su LEFT JOIN sys_user_role sur ON su.gid = sur.user_gid AND sur.is_valid = 1 LEFT JOIN sys_role sr ON sur.role_id = sr.id WHERE su.is_valid = 1");
        if (StringUtils.isNotBlank(bean.getUserName())) {
            sql.append(" AND su.user_name = ?");
            paramList.add(bean.getUserName());
        }
        if (StringUtils.isNotBlank(bean.getPhone())) {
            sql.append(" AND su.phone = ?");
            paramList.add(bean.getPhone());
        }
        if (StringUtils.isNotBlank(bean.getRoleLabel())) {
            sql.append(" AND sr.role_label = ?");
            paramList.add(bean.getRoleLabel());
        }
        if (!isCountSql && bean.getSortLabel() != null && bean.getSortOrder() != null && bean.columnMap(true).containsKey(bean.getSortLabel())) {
            sql.append(" ORDER BY su.").append(bean.getSortLabel()).append("asc".equalsIgnoreCase(bean.getSortOrder()) ? " ASC" : " DESC");
        }
        return sql.toString();
    }
}