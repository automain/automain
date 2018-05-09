package com.github.automain.monitor.vo;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class DbPagesVO extends RequestUtil implements BaseBean<DbPagesVO> {

    // 已用页大小
    private Long pagesData;

    // 空闲页大小
    private Long pagesFree;

    // 忙碌页大小
    private Long pagesMisc;

    // 创建时间
    private Timestamp createTime;

    // 连接池名称
    private String poolName;

    public Long getPagesData() {
        return pagesData;
    }

    public void setPagesData(Long pagesData) {
        this.pagesData = pagesData;
    }

    public Long getPagesFree() {
        return pagesFree;
    }

    public void setPagesFree(Long pagesFree) {
        this.pagesFree = pagesFree;
    }

    public Long getPagesMisc() {
        return pagesMisc;
    }

    public void setPagesMisc(Long pagesMisc) {
        this.pagesMisc = pagesMisc;
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
    public Map<String, Object> notNullColumnMap() {
        return null;
    }

    @Override
    public DbPagesVO beanFromRequest(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public DbPagesVO beanFromResultSet(ResultSet rs) throws SQLException {
        DbPagesVO bean = new DbPagesVO();
        bean.setPagesData(rs.getLong("pages_data"));
        bean.setPagesFree(rs.getLong("pages_free"));
        bean.setPagesMisc(rs.getLong("pages_misc"));
        bean.setCreateTime(rs.getTimestamp("create_time"));
        bean.setPoolName(rs.getString("pool_name"));
        return bean;
    }
}
