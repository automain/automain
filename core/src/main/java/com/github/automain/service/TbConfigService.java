package com.github.automain.service;

import com.github.automain.bean.TbConfig;
import com.github.automain.dao.TbConfigDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbConfigService extends BaseService<TbConfig, TbConfigDao> {

    public TbConfigService(TbConfig bean, TbConfigDao dao) {
        super(bean, dao);
    }

    public PageBean<TbConfig> selectTableForCustomPage(ConnectionBean connection, TbConfig bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }
}