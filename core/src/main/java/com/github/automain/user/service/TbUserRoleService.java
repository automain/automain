package com.github.automain.user.service;

import com.github.automain.user.bean.TbUserRole;
import com.github.automain.user.dao.TbUserRoleDao;
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
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }

    public Set<Long> selectUserIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectUserIdByRoleId(connection, roleId);
    }

    public int clearUserRoleByUserId(ConnectionBean connection, Long userId) throws SQLException {
        return getDao().clearUserRoleByUserId(connection, userId);
    }

}