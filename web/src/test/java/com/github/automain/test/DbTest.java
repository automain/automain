package com.github.automain.test;

import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbTest.class);

    public static void main(String[] args) {
        Connection connection = null;
        try {
            SystemUtil.initConnectionPool();
            connection = ConnectionPool.getConnection(null);
            // doTest
            LOGGER.info("test start");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
