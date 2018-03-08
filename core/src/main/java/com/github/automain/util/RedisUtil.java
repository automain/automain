package com.github.automain.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUtil {

    private RedisUtil() {
    }

    private static class InitPool {

        private static boolean openRedis;

        private static JedisPool pool = null;

        static {
            Properties redisConfig = PropertiesUtil.getProperties("redis.properties");
            openRedis = Boolean.parseBoolean(redisConfig.getProperty("openRedis", "false"));
            if (openRedis) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(Integer.parseInt(redisConfig.getProperty("maxTotal", "150")));
                config.setMaxIdle(Integer.parseInt(redisConfig.getProperty("maxIdle", "30")));
                config.setMinIdle(Integer.parseInt(redisConfig.getProperty("minIdle", "10")));
                config.setMaxWaitMillis(Long.parseLong(redisConfig.getProperty("maxWaitMillis", "3000")));
                config.setTestOnBorrow(Boolean.parseBoolean(redisConfig.getProperty("testOnBorrow", "false")));
                config.setTestOnReturn(Boolean.parseBoolean(redisConfig.getProperty("testOnReturn", "true")));
                config.setTestWhileIdle(Boolean.parseBoolean(redisConfig.getProperty("testWhileIdle", "true")));
                config.setMinEvictableIdleTimeMillis(Long.parseLong(redisConfig.getProperty("minEvictableIdleTimeMillis", "60000")));
                config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(redisConfig.getProperty("softMinEvictableIdleTimeMillis", "1000")));
                config.setTimeBetweenEvictionRunsMillis(Long.parseLong(redisConfig.getProperty("setTimeBetweenEvictionRunsMillis", "1000")));
                config.setNumTestsPerEvictionRun(Integer.parseInt(redisConfig.getProperty("setNumTestsPerEvictionRun", "100")));
                String host = redisConfig.getProperty("host");
                int port = Integer.parseInt(redisConfig.getProperty("port", "6379"));
                int timeout = Integer.parseInt(redisConfig.getProperty("timeout", "5000"));
                pool = new JedisPool(config, host, port, timeout);
            }
        }
    }

    private static JedisPool getPool() {
        return InitPool.pool;
    }

    /**
     * 获取redis实例，redis关闭时返回空
     *
     * @return
     */
    public static Jedis getJedis() {
        return getPool() == null ? null : getPool().getResource();
    }

    /**
     * 关闭redis时使用的本地缓存
     */
    public static Map<String, Object> LOCAL_CACHE = new ConcurrentHashMap<String, Object>();

    /**
     * 获取分布式锁
     *
     * @param jedis
     * @param lockKey
     * @param expireSeconds
     * @return
     */
    public static boolean getDistributeLock(Jedis jedis, String lockKey, int expireSeconds) {
        if (jedis != null) {
            long expireTime = SystemUtil.getNowSecond() + expireSeconds;
            long result = jedis.setnx(lockKey, String.valueOf(expireTime));
            if (result == 1) {
                jedis.expire(lockKey, expireSeconds);
                return true;
            } else {
                if (jedis.ttl(lockKey) > 0) {
                    return false;
                } else {
                    jedis.del(lockKey);
                    result = jedis.setnx(lockKey, String.valueOf(expireTime));
                    return result == 1;
                }
            }
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis
     * @param lockKey
     * @return
     */
    public static boolean releaseDistributeLock(Jedis jedis, String lockKey) {
        if (jedis != null) {
            jedis.del(lockKey);
        }
        return false;
    }
}
