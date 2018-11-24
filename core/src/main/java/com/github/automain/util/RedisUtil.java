package com.github.automain.util;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class RedisUtil {

    private static JedisPool POOL = null;

    private static RedissonClient REDISSON = null;

    private static ReentrantLock LOCAL_LOCK = new ReentrantLock();

    /**
     * 初始化redis连接池
     */
    public static void initJedisPool() {
        Properties redisConfig = PropertiesUtil.getProperties("redis.properties");
        boolean openRedis = Boolean.parseBoolean(redisConfig.getProperty("openRedis", "false"));
        if (openRedis) {
            // 初始化jedis
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
            POOL = new JedisPool(config, host, port, timeout);
            // 初始化redisson
            Config redissonConfig = new Config();
            SingleServerConfig singleSerververConfig = redissonConfig.useSingleServer();
            singleSerververConfig.setAddress("redis://" + host + ":" + port);
            REDISSON = Redisson.create(redissonConfig);
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
     * @param lockKey
     * @param expireSeconds
     * @return
     */
    public static boolean getDistributeLock(String lockKey, int expireSeconds) throws InterruptedException {
        if (REDISSON != null) {
            RLock lock = REDISSON.getLock(lockKey);
            return lock.tryLock(10, expireSeconds, TimeUnit.SECONDS);
        } else {
            return LOCAL_LOCK.tryLock(expireSeconds, TimeUnit.SECONDS);
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey
     * @return
     */
    public static void releaseDistributeLock(String lockKey) {
        if (REDISSON != null) {
            RLock lock = REDISSON.getLock(lockKey);
            lock.unlock();
        } else {
            LOCAL_LOCK.unlock();
        }
    }
}
