package com.github.automain.common.dao;

import com.github.automain.common.bean.TbInnerIpPort;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TbInnerIpPortDao extends BaseDao<TbInnerIpPort> {

    @SuppressWarnings("unchecked")
    public PageBean<TbInnerIpPort> selectTableForCustomPage(ConnectionBean connection, TbInnerIpPort bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbInnerIpPort bean, List<Object> parameterList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_inner_ip_port WHERE is_delete = 0 ");
        if (bean.getIp() != null) {
            sql.append(" AND ip = ?");
            parameterList.add(bean.getIp());
        }
        if (bean.getPort() != null) {
            sql.append(" AND port = ?");
            parameterList.add(bean.getPort());
        }
        return sql.toString();
    }

    public List<String> selectInnerIpList(ConnectionBean connection) throws SQLException {
        ResultSet rs = null;
        List<String> ipList = new ArrayList<String>();
        try {
            String sql = "SELECT DISTINCT ip FROM tb_inner_ip_port WHERE is_delete = 0";
            rs = executeSelectReturnResultSet(connection, sql, new ArrayList<>());
            while (rs.next()) {
                ipList.add(rs.getString(1));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return ipList;
    }

    public List<String> selectInnerIpPortList(ConnectionBean connection) throws SQLException {
        ResultSet rs = null;
        List<String> ipPortList = new ArrayList<String>();
        try {
            String sql = "SELECT ip, port FROM tb_inner_ip_port WHERE is_delete = 0";
            rs = executeSelectReturnResultSet(connection, sql, new ArrayList<>());
            while (rs.next()) {
                ipPortList.add(rs.getString("ip") + ":" + rs.getString("port"));
            }
        } finally {
            ConnectionPool.close(rs);
        }
        return ipPortList;
    }

}