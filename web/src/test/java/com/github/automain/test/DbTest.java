package com.github.automain.test;

import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbTest.class);

    public static void main(String[] args) {
        try {
            SystemUtil.initConnectionPool();
            ConnectionPool.getConnection(null);
            // doTest
            LOGGER.info("test start");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
