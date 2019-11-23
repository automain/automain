package com.github.automain.common.controller;

import com.github.automain.dao.SysScheduleDao;
import com.github.automain.util.DateUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.sql.Connection;

public abstract class BaseSchedule implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSchedule.class);

    private String scheduleUrl;

    private Integer period;

    private Integer startExecuteTime;

    public BaseSchedule(String scheduleUrl, Integer period, Integer startExecuteTime) {
        this.scheduleUrl = scheduleUrl;
        this.period = period;
        this.startExecuteTime = startExecuteTime;
    }

    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public Integer getPeriod() {
        return period;
    }

    public Integer getStartExecuteTime() {
        return startExecuteTime;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        Connection connection = null;
        try {
            LOGGER.info("schedule execute start uri = {}", scheduleUrl);
            int expireSeconds = period > 59 ? 50 : (int) (period / 2);
            String lockKey = "SCHEDULE_" + scheduleUrl;
            jedis = RedisUtil.getJedis();
            boolean lock = RedisUtil.getDistributeLock(jedis,lockKey, expireSeconds);
            if (lock) {
                LOGGER.info("schedule get lock success uri = {}", scheduleUrl);
                connection = ConnectionPool.getConnection(DispatcherController.SLAVE_POOL_MAP.get(scheduleUrl));
                execute(connection, jedis);
                if (connection.isReadOnly()) {
                    Connection writeConnection = null;
                    try {
                        writeConnection = ConnectionPool.getConnection(null);
                        SysScheduleDao.updateLastExecuteTime(writeConnection, scheduleUrl, DateUtil.getNow());
                    } finally {
                        ConnectionPool.close(writeConnection);
                    }
                } else {
                    SysScheduleDao.updateLastExecuteTime(connection, scheduleUrl, DateUtil.getNow());
                }
                LOGGER.info("schedule execute end uri = {}", scheduleUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SystemUtil.closeJedisAndConnection(jedis, connection);
        }
    }

    protected abstract void execute(Connection connection, Jedis jedis) throws Exception;
}
