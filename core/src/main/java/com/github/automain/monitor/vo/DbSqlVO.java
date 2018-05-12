package com.github.automain.monitor.vo;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class DbSqlVO extends RequestUtil implements BaseBean<DbSqlVO> {

    // 删除数
    private Long comDelete;

    // 添加数
    private Long comInsert;

    // 查询数
    private Long comSelect;

    // 编辑数
    private Long comUpdate;

    // 创建时间
    private Timestamp createTime;

    // 连接池名称
    private String poolName;

    public Long getComDelete() {
        return comDelete;
    }

    public void setComDelete(Long comDelete) {
        this.comDelete = comDelete;
    }

    public Long getComInsert() {
        return comInsert;
    }

    public void setComInsert(Long comInsert) {
        this.comInsert = comInsert;
    }

    public Long getComSelect() {
        return comSelect;
    }

    public void setComSelect(Long comSelect) {
        this.comSelect = comSelect;
    }

    public Long getComUpdate() {
        return comUpdate;
    }

    public void setComUpdate(Long comUpdate) {
        this.comUpdate = comUpdate;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
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
    public Map<String, Object> columnMap(boolean all) {
        return null;
    }

    @Override
    public DbSqlVO beanFromRequest(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public DbSqlVO beanFromResultSet(ResultSet rs) throws SQLException {
        DbSqlVO bean = new DbSqlVO();
        bean.setComDelete(rs.getLong("com_delete"));
        bean.setComInsert(rs.getLong("com_insert"));
        bean.setComSelect(rs.getLong("com_select"));
        bean.setComUpdate(rs.getLong("com_update"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPoolName(rs.getString("pool_name"));
        return bean;
    }
}
