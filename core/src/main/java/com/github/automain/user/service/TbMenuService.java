package com.github.automain.user.service;

import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.dao.TbMenuDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbMenuService extends BaseService<TbMenu, TbMenuDao> {

    public TbMenuService(TbMenu bean, TbMenuDao dao) {
        super(bean, dao);
    }

    public PageBean<TbMenu> selectTableForCustomPage(ConnectionBean connection, TbMenu bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }

    public List<TbMenu> selectTbMenuByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectTbMenuByRoleId(connection, getBean(), roleId);
    }
}