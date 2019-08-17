package com.github.automain.service;

import com.github.automain.bean.TbUserRole;
import com.github.automain.dao.TbUserRoleDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Set;

public class TbUserRoleService extends BaseService<TbUserRole, TbUserRoleDao> {

    public TbUserRoleService(TbUserRole bean, TbUserRoleDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUserRole> selectTableForCustomPage(ConnectionBean connection, TbUserRole bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean,  pageFromRequest(request), limitFromRequest(request));
    }

    public Set<Long> selectUserIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectUserIdByRoleId(connection, roleId);
    }

    public int clearUserRoleByUserId(ConnectionBean connection, Long userId) throws SQLException {
        return getDao().clearUserRoleByUserId(connection, userId);
    }

}