package com.github.automain.service;

import com.github.automain.bean.TbRolePrivilege;
import com.github.automain.dao.TbRolePrivilegeDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class TbRolePrivilegeService extends BaseService<TbRolePrivilege, TbRolePrivilegeDao> {

    public TbRolePrivilegeService(TbRolePrivilege bean, TbRolePrivilegeDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRolePrivilege> selectTableForCustomPage(ConnectionBean connection, TbRolePrivilege bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public Set<String> selectPrivilegeLabelByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectPrivilegeLabelByRoleId(connection, roleId);
    }

    public List<Long> selectPrivilegeIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectPrivilegeIdByRoleId(connection, roleId);
    }

    public int clearRoleByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().clearRoleByRoleId(connection, roleId);
    }
}