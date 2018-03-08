package com.github.automain.user.service;

import com.github.automain.user.bean.TbUser;
import com.github.automain.user.dao.TbUserDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbUserService extends BaseService<TbUser, TbUserDao> {

    public TbUserService(TbUser bean, TbUserDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUser> selectTableForCustomPage(ConnectionBean connection, TbUser bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }
}