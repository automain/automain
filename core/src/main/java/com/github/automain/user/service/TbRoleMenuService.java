package com.github.automain.user.service;

import com.github.automain.user.bean.TbRoleMenu;
import com.github.automain.user.dao.TbRoleMenuDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbRoleMenuService extends BaseService<TbRoleMenu, TbRoleMenuDao> {

    public TbRoleMenuService(TbRoleMenu bean, TbRoleMenuDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRoleMenu> selectTableForCustomPage(ConnectionBean connection, TbRoleMenu bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }
}