package com.github.automain.monitor.service;

import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.monitor.dao.DbStatusDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class DbStatusService extends BaseService<DbStatus, DbStatusDao> {

    public DbStatusService(DbStatus bean, DbStatusDao dao) {
        super(bean, dao);
    }

    public PageBean<DbStatus> selectTableForCustomPage(ConnectionBean connection, DbStatus bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }
}