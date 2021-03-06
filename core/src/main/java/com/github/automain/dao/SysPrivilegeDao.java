package com.github.automain.dao;

import com.github.automain.bean.SysPrivilege;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.SysPrivilegeVO;
import com.github.fastjdbc.BaseDao;
import com.github.fastjdbc.PageBean;
import com.github.fastjdbc.PageParamBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SysPrivilegeDao extends BaseDao {

    private static final SysPrivilege DEFAULT_BEAN = new SysPrivilege();

    public static int deleteTableByIdList(List<Integer> idList) throws SQLException {
        return deleteTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysPrivilege> selectTableByIdList(List<Integer> idList) throws SQLException {
        return selectTableByIdList(DEFAULT_BEAN, idList);
    }

    public static List<SysPrivilege> selectAllTable() throws SQLException {
        return selectAllTable(DEFAULT_BEAN);
    }

    @SuppressWarnings("unchecked")
    public static PageBean<SysPrivilege> selectTableForCustomPage(SysPrivilegeVO bean) throws Exception {
        List<Object> countParamList = new ArrayList<Object>();
        List<Object> paramList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParamList, true);
        String sql = setSearchCondition(bean, paramList, false);
        PageParamBean<SysPrivilege> pageParamBean = new PageParamBean<SysPrivilege>()
                .setBean(bean)
                .setCountSql(countSql)
                .setCountParamList(countParamList)
                .setSql(sql)
                .setParamList(paramList)
                .setPage(bean.getPage())
                .setSize(bean.getSize());
        return selectTableForPage(pageParamBean);
    }

    private static String setSearchCondition(SysPrivilegeVO bean, List<Object> paramList, boolean isCountSql) {
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

    public static Set<String> selectUserPrivilege(String userGid) throws SQLException {
        return new HashSet<String>(executeSelectReturnStringList("SELECT sp.privilege_label FROM sys_user_role sur INNER JOIN sys_role_privilege srp ON sur.role_id = srp.role_id INNER JOIN sys_privilege sp ON srp.privilege_id = sp.id WHERE sur.is_valid = 1 AND srp.is_valid = 1 AND sur.user_gid = ? GROUP BY sp.privilege_label", List.of(userGid)));
    }

    public static List<IdNameVO> selectAllPrivilege() throws SQLException {
        return executeSelectReturnList("SELECT sp.id, sp.privilege_name AS 'name' FROM sys_privilege sp", null, new IdNameVO());
    }
}