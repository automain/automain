package com.github.automain.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUtil {

    private static JedisPool pool;

    public static JedisPool getPool() {
        return pool;
    }

    public static void setPool(JedisPool pool) {
        RedisUtil.pool = pool;
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
    private static Map<String, Object> LOCAL_CACHE = new ConcurrentHashMap<String, Object>();

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
    public static boolean getDistributeLock(Jedis jedis, String lockKey, int expireSeconds) {
        int now = SystemUtil.getNowSecond();
        int expireTime = now + expireSeconds;
        if (jedis != null) {
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
        } else {
            Long expire = getLocalCache(lockKey);
            if (expire == null) {
                setLocalCache(lockKey, expireTime);
                return true;
            } else {
                if (expire > now) {
                    return false;
                } else {
                    setLocalCache(lockKey, expireTime);
                    return true;
                }
            }
        }
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
        } else {
            delLocalCache(lockKey);
        }
        return false;
    }
}
