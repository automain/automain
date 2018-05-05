package com.github.automain.monitor.dao;

import com.github.automain.monitor.bean.DbStatus;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class DbStatusDao extends BaseDao<DbStatus> {

    @SuppressWarnings("unchecked")
    public PageBean<DbStatus> selectTableForCustomPage(ConnectionBean connection, DbStatus bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(DbStatus bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM db_status WHERE 1 = 1 ");
        if (bean.getCreateTimeRange() != null) {
            sql.append(" AND create_time >= ? AND create_time <= ?");
            setTimeRange(bean.getCreateTimeRange(), parameterList);
        }
        if (bean.getComCommit() != null) {
            sql.append(" AND com_commit = ?");
            parameterList.add(bean.getComCommit());
        }
        if (bean.getComDelete() != null) {
            sql.append(" AND com_delete = ?");
            parameterList.add(bean.getComDelete());
        }
        if (bean.getComInsert() != null) {
            sql.append(" AND com_insert = ?");
            parameterList.add(bean.getComInsert());
        }
        if (bean.getComRollback() != null) {
            sql.append(" AND com_rollback = ?");
            parameterList.add(bean.getComRollback());
        }
        if (bean.getComSelect() != null) {
            sql.append(" AND com_select = ?");
            parameterList.add(bean.getComSelect());
        }
        if (bean.getComUpdate() != null) {
            sql.append(" AND com_update = ?");
            parameterList.add(bean.getComUpdate());
        }
        if (bean.getPagesDataSize() != null) {
            sql.append(" AND pages_data_size = ?");
            parameterList.add(bean.getPagesDataSize());
        }
        if (bean.getPagesFreeSize() != null) {
            sql.append(" AND pages_free_size = ?");
            parameterList.add(bean.getPagesFreeSize());
        }
        if (bean.getPagesMiscSize() != null) {
            sql.append(" AND pages_misc_size = ?");
            parameterList.add(bean.getPagesMiscSize());
        }
        if (bean.getPagesTotalSize() != null) {
            sql.append(" AND pages_total_size = ?");
            parameterList.add(bean.getPagesTotalSize());
        }
        if (bean.getPoolName() != null) {
            sql.append(" AND pool_name = ?");
            parameterList.add(bean.getPoolName());
        }
        if (bean.getThreadsConnected() != null) {
            sql.append(" AND threads_connected = ?");
            parameterList.add(bean.getThreadsConnected());
        }
        if (bean.getThreadsRunning() != null) {
            sql.append(" AND threads_running = ?");
            parameterList.add(bean.getThreadsRunning());
        }
        return sql.toString();
    }
}