package com.github.automain.test;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

public class DbTest {

    public static void main(String[] args) {
        ConnectionBean connection = null;
        try {
            SystemUtil.initConnectionPool();
            connection = ConnectionPool.getConnectionBean(null);
            Long start = DateUtil.getMinDayTimestamp(System.currentTimeMillis()).getTime();
            Long end = start + 86400000;
            DbStatus status = new DbStatus();
            Random random = new Random();
            for (;start < end; start += 5000){
               for (String poolName : PropertiesUtil.POOL_NAMES) {
                   status.setPoolName(poolName);
                   status.setCreateTime(new Timestamp(start));
                   status.setComSelect((long) random.nextInt(1000));
                   status.setComInsert((long) random.nextInt(1000));
                   status.setComUpdate((long) random.nextInt(1000));
                   status.setComDelete((long) random.nextInt(1000));
                   status.setComCommit((long) random.nextInt(1000));
                   status.setComRollback((long) random.nextInt(1000));
                   status.setThreadsFree((long) random.nextInt(1000));
                   status.setThreadsRunning((long) random.nextInt(1000));
                   status.setPagesData((long) random.nextInt(1000));
                   status.setPagesFree((long) random.nextInt(1000));
                   status.setPagesMisc((long) random.nextInt(1000));
                   ServiceContainer.DB_STATUS_SERVICE.insertIntoTable(connection, status);
               }
            }
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
