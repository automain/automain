package com.github.automain.common.controller;

import com.github.automain.common.bean.JsonResponse;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.ConnectionPool;
import redis.clients.jedis.Jedis;

import java.sql.Connection;

public abstract class BaseSchedule implements Runnable {

    private String scheduleUrl;

    private long period;

    public BaseSchedule(String scheduleUrl, long period) {
        this.scheduleUrl = scheduleUrl;
        this.period = period;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        Connection connection = null;
        try {
            int expireSeconds = period > 59 ? 50 : (int) (period / 2);
            String lockKey = "SCHEDULE_" + scheduleUrl;
            boolean lock = RedisUtil.getDistributeLock(lockKey, expireSeconds);
            if (lock) {
                jedis = RedisUtil.getJedis();
                connection = ConnectionPool.getConnection(DispatcherController.SLAVE_POOL_MAP.get(scheduleUrl));
                execute(connection, jedis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SystemUtil.closeJedisAndConnection(jedis, connection);
        }
    }

    protected abstract JsonResponse execute(Connection connection, Jedis jedis) throws Exception;
}
