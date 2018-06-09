package com.github.automain.common.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbInnerIpPort extends RequestUtil implements BaseBean<TbInnerIpPort> {

    // 内部地址ID
    private Long innerId;

    // 内部地址
    private String ip;

    // 端口号
    private String port;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getInnerId() {
        return innerId;
    }

    public void setInnerId(Long innerId) {
        this.innerId = innerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_inner_ip_port";
    }

    @Override
    public String primaryKey() {
        return "inner_id";
    }

    @Override
    public Long primaryValue() {
        return this.getInnerId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getIp() != null) {
            map.put("ip", this.getIp());
        }
        if (all || this.getPort() != null) {
            map.put("port", this.getPort());
        }
        if (all || this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbInnerIpPort beanFromResultSet(ResultSet rs) throws SQLException {
        TbInnerIpPort bean = new TbInnerIpPort();
        bean.setInnerId(rs.getLong("inner_id"));
        bean.setIp(rs.getString("ip"));
        bean.setPort(rs.getString("port"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbInnerIpPort beanFromRequest(HttpServletRequest request) {
        TbInnerIpPort bean = new TbInnerIpPort();
        bean.setInnerId(getLong("innerId", request));
        bean.setIp(getString("ip", request));
        bean.setPort(getString("port", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}