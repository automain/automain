package com.github.automain.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class RedisUtil {

    private static JedisPool POOL = null;

    private static ReentrantLock LOCAL_LOCK = new ReentrantLock();

    /**
     * 初始化redis连接池
     */
    public static void initJedisPool() {
        boolean openRedis = PropertiesUtil.getBooleanProperty("redis.open");
        if (openRedis) {
            // 初始化jedis
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(PropertiesUtil.getIntProperty("redis.maxTotal", "150"));
            config.setMaxIdle(PropertiesUtil.getIntProperty("redis.maxIdle", "30"));
            config.setMinIdle(PropertiesUtil.getIntProperty("redis.minIdle", "10"));
            config.setMaxWaitMillis(PropertiesUtil.getLongProperty("redis.maxWaitMillis", "3000"));
            config.setTestOnBorrow(PropertiesUtil.getBooleanProperty("redis.testOnBorrow"));
            config.setTestOnReturn(PropertiesUtil.getBooleanProperty("redis.testOnReturn"));
            config.setTestWhileIdle(PropertiesUtil.getBooleanProperty("redis.testWhileIdle", "true"));
            config.setMinEvictableIdleTimeMillis(PropertiesUtil.getLongProperty("redis.minEvictableIdleTimeMillis", "60000"));
            config.setSoftMinEvictableIdleTimeMillis(PropertiesUtil.getLongProperty("redis.softMinEvictableIdleTimeMillis", "1000"));
            config.setTimeBetweenEvictionRunsMillis(PropertiesUtil.getLongProperty("redis.setTimeBetweenEvictionRunsMillis", "1000"));
            config.setNumTestsPerEvictionRun(PropertiesUtil.getIntProperty("redis.setNumTestsPerEvictionRun", "100"));
            String host = PropertiesUtil.getStringProperty("redis.host");
            int port = PropertiesUtil.getIntProperty("redis.port", "6379");
            int timeout = PropertiesUtil.getIntProperty("redis.timeout", "5000");
            POOL = new JedisPool(config, host, port, timeout);
        }
    }
    /**
     * 获取redis实例，redis关闭时返回空
     *
     * @return
     */
    public static Jedis getJedis() {
        return POOL == null ? null : POOL.getResource();
    }

    /**
     * 关闭redis时使用的本地缓存
     */
    private static final Map<String, Object> LOCAL_CACHE = new ConcurrentHashMap<String, Object>();

    /**
     * 获取本地缓存
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static<T> T getLocalCache(String key) {
        return (T) LOCAL_CACHE.get(key);
    }

    /**
     * 设置本地缓存
     * @param key
     * @param value
     */
    public static void setLocalCache(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

    /**
     * 删除本地缓存
     * @param key
     */
    public static void delLocalCache(String key) {
        LOCAL_CACHE.remove(key);
    }

    /**
     * 获取分布式锁
     *
     * @param jedis
     * @param lockKey
     * @param expireSeconds
     * @return
     */
    public static boolean getDistributeLock(Jedis jedis, String lockKey, int expireSeconds) throws InterruptedException {
        if (jedis != null) {
            return jedis.set(lockKey, lockKey,SetParams.setParams().ex(expireSeconds).nx()) != null;
        } else {
            return LOCAL_LOCK.tryLock(expireSeconds, TimeUnit.SECONDS);
        }
    }

    /**
     * 释放分布式锁
     *
     * @param jedis
     * @param lockKey
     * @return
     */
    public static void releaseDistributeLock(Jedis jedis, String lockKey) {
        if (jedis != null) {
            jedis.del(lockKey);
        } else {
            LOCAL_LOCK.unlock();
        }
    }
}
