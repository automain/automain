package com.github.automain.monitor.service;

import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.monitor.dao.DbStatusDao;
import com.github.automain.monitor.vo.DbPagesVO;
import com.github.automain.monitor.vo.DbSqlVO;
import com.github.automain.monitor.vo.DbThreadVO;
import com.github.automain.monitor.vo.DbTransactionVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DbStatusService extends BaseService<DbStatus, DbStatusDao> {

    public DbStatusService(DbStatus bean, DbStatusDao dao) {
        super(bean, dao);
    }

    public PageBean<DbStatus> selectTableForCustomPage(ConnectionBean connection, DbStatus bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public DbStatus selectNowStatus(ConnectionBean connection, String poolName) throws SQLException {
        return getDao().selectNowStatus(connection, poolName);
    }

    public List<DbSqlVO> selectDbSql(ConnectionBean connection, Timestamp startTime, Timestamp endTime) throws SQLException {
        return getDao().selectDbSql(connection, startTime, endTime);
    }

    public List<DbTransactionVO> selectDbTransaction(ConnectionBean connection, Timestamp startTime, Timestamp endTime, String masterName) throws SQLException {
        return getDao().selectDbTransaction(connection, startTime, endTime, masterName);
    }

    public List<DbThreadVO> selectDbThread(ConnectionBean connection, Timestamp startTime, Timestamp endTime) throws SQLException {
        return getDao().selectDbThread(connection, startTime, endTime);
    }

    public List<DbPagesVO> selectDbPages(ConnectionBean connection, Timestamp startTime, Timestamp endTime) throws SQLException {
        return getDao().selectDbPages(connection, startTime, endTime);
    }
}