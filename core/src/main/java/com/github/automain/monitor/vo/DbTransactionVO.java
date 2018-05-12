package com.github.automain.monitor.vo;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class DbTransactionVO extends RequestUtil implements BaseBean<DbTransactionVO> {

    // 事务数
    private Long transaction;

    // 创建时间
    private Timestamp createTime;

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public String primaryKey() {
        return null;
    }

    @Override
    public Long primaryValue() {
        return null;
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        return null;
    }

    @Override
    public DbTransactionVO beanFromRequest(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public DbTransactionVO beanFromResultSet(ResultSet rs) throws SQLException {
        DbTransactionVO bean = new DbTransactionVO();
        bean.setTransaction(rs.getLong("com_commit") - rs.getLong("com_rollback"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        return bean;
    }

}
