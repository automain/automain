package com.github.automain.service;

import com.github.automain.bean.TbRoleMenu;
import com.github.automain.dao.TbRoleMenuDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbRoleMenuService extends BaseService<TbRoleMenu, TbRoleMenuDao> {

    public TbRoleMenuService(TbRoleMenu bean, TbRoleMenuDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRoleMenu> selectTableForCustomPage(ConnectionBean connection, TbRoleMenu bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean,  pageFromRequest(request), limitFromRequest(request));
    }

    public int clearRoleByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().clearRoleByRoleId(connection, roleId);
    }

    public List<Long> selectMenuIdByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectMenuIdByRoleId(connection, roleId);
    }
}