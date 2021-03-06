package com.github.automain.dao;

import com.github.automain.bean.SysUser;
import com.github.automain.vo.SysUserAddVO;
import com.github.automain.vo.SysUserVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysUserDao extends BaseDao {

    private static final SysUser DEFAULT_BEAN = new SysUser();

    public static int softDeleteTableByIdList(List<Integer> idList) throws SQLException {
        return softDeleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int softDeleteTableByGidList(List<String> gidList) throws SQLException {
        return softDeleteTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static int deleteTableByGidList(List<String> gidList) throws SQLException {
        return deleteTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static List<SysUser> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysUser> selectTableByGidList(List<String> gidList) throws SQLException {
        return selectTableByGidList(DEFAULT_BEAN, gidList);
    }

    public static List<SysUser> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysUserAddVO> selectTableForCustomPage(SysUserVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysUserAddVO> pageParamBean = new PageParamBean<SysUserAddVO>()
                .setBean(new SysUserAddVO())
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
        sql.append(isCountSql ? "COUNT(DISTINCT su.gid)" : "su.gid,su.create_time,su.update_time,su.user_name,su.real_name,su.phone,su.email,su.head_img_gid,GROUP_CONCAT(sr.role_name) AS 'role_name',CONCAT('/uploads', sf.file_path) AS 'head_img'")
                .append(" FROM sys_user su LEFT JOIN sys_user_role sur ON su.gid = sur.user_gid AND sur.is_valid = 1 LEFT JOIN sys_role sr ON sur.role_id = sr.id LEFT JOIN sys_file sf ON su.head_img_gid = sf.gid WHERE su.is_valid = 1");
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
        if (!isCountSql) {
            sql.append(" GROUP BY su.gid");
        }
        return sql.toString();
    }

    public static boolean checkUserNameUseable(String userName, String gid) throws SQLException {
        String existGid = executeSelectReturnString("SELECT su.gid FROM sys_user su WHERE su.user_name = ? AND su.is_valid = 1 LIMIT 1", List.of(userName));
        return existGid == null || existGid.equals(gid);
    }

    public static SysUserAddVO selectUserVOByGid(String gid) throws SQLException {
        return executeSelectReturnBean("SELECT su.gid,su.create_time,su.update_time,su.user_name,su.real_name,su.phone,su.email,su.head_img_gid,CONCAT('/uploads', sf.file_path) AS 'head_img', '' AS 'role_name' FROM sys_user su LEFT JOIN sys_file sf ON su.head_img_gid = sf.gid WHERE su.gid = ?", List.of(gid), new SysUserAddVO());
    }
}