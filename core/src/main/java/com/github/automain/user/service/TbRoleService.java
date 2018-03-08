package com.github.automain.user.service;

import com.github.automain.user.bean.TbRole;
import com.github.automain.user.dao.TbRoleDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbRoleService extends BaseService<TbRole, TbRoleDao> {

    public TbRoleService(TbRole bean, TbRoleDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRole> selectTableForCustomPage(ConnectionBean connection, TbRole bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }
}