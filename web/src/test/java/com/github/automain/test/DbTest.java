package com.github.automain.test;

import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;

import java.sql.SQLException;

public class DbTest {

    public static void main(String[] args) {
        ConnectionBean connection = null;
        try {
            SystemUtil.initConnectionPool();
            connection = ConnectionPool.getConnectionBean(null);
            // doTest
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
