package com.github.automain.monitor.service;

import com.github.automain.monitor.bean.DbSlowLog;
import com.github.automain.monitor.dao.DbSlowLogDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class DbSlowLogService extends BaseService<DbSlowLog, DbSlowLogDao> {

    public DbSlowLogService(DbSlowLog bean, DbSlowLogDao dao) {
        super(bean, dao);
    }

    public PageBean<DbSlowLog> selectTableForCustomPage(ConnectionBean connection, DbSlowLog bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public List<DbSlowLog> selectNowSlowSql(ConnectionBean connection, String poolName) throws SQLException {
        return getDao().selectNowSlowSql(connection, getBean(), poolName);
    }
}