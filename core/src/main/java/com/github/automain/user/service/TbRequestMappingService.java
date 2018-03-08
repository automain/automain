package com.github.automain.user.service;

import com.github.automain.user.bean.TbRequestMapping;
import com.github.automain.user.dao.TbRequestMappingDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbRequestMappingService extends BaseService<TbRequestMapping, TbRequestMappingDao> {

    public TbRequestMappingService(TbRequestMapping bean, TbRequestMappingDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRequestMapping bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }
}