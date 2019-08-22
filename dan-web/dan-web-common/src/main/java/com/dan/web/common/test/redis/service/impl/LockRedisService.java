package com.dan.web.common.test.redis.service.impl;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: RedisServiceNewImpl
 * @createDate: 2019-08-22 09:26.
 * @description:
 */
public class LockRedisService {

    private JedisPool jedisPool;

    public LockRedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 同时在redis创建相同的一个key,相同key名称
     */
    private final static String REDIS_LOCK_KEY = "redis_lock";


    public String getRedisLock(Long acquireTimeOut, Long timeOut) {
        return getRedisLock(acquireTimeOut, timeOut, REDIS_LOCK_KEY);
    }

    /**
     * 基于redis实现分布式锁代码思路核心方法获取锁、释放锁
     * redis 以key (redislockKey) 和value ( 随机不能够重复数字锁的id)方式进行存储
     * redis实现分布式锁有两个超时时间问题
     *
     * @param acquireTimeOut 获取锁的超时时间-毫秒----在尝试获取锁的时候，如果在规定的时间内还没有获取锁，直接放弃
     * @param timeOut        锁的有效时间-毫秒---当获取锁成功之后，对应的key,有对应有效期,对应的key,在规定时间内失效
     */
    public String getRedisLock(Long acquireTimeOut, Long timeOut, String redisLockKey) {
        if (StringUtils.isBlank(redisLockKey)) {
            return null;
        }
        Jedis conn = null;
        try {
            // 1.建立redis连接
            conn = jedisPool.getResource();

            // 2.定义redis对应key的value值( uuid) 作用释放锁
            String identifier = UUID.randomUUID().toString();

            // 3.获取锁的有效时间-(锁到了这个时间会自动销毁掉)

            //传过来是毫秒,redis是以秒为单位
            int expireLock = timeOut.intValue() / 1000;

            // 4.获取锁的超时时间(在这个范围内没有获取到锁,表示获取锁失败)
            long endTime = System.currentTimeMillis() + acquireTimeOut;
            // 5.使用循环机制如果没有获取到锁，要在规定acquireTimeout时间保证重复进行尝试获取锁(乐观锁)
            while (System.currentTimeMillis() < endTime) {
                //获取锁
                //6.使用setnx命令插入对应的redisLockKey，如果返回为1成功获取锁
                if (conn.setnx(redisLockKey, identifier) == 1) {
                    //设置对应key的有效期
                    conn.expire(redisLockKey, expireLock);
                    return identifier;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return null;
    }

    public boolean unRedisLock(String identifier) {
        return unRedisLock(REDIS_LOCK_KEY, identifier);
    }

    /**
     * 释放redis锁
     *
     * @param lockKey    redis key
     * @param identifier redis 标识-key的值
     */
    public boolean unRedisLock(String lockKey, String identifier) {
        if (StringUtils.isBlank(lockKey)) {
            return false;
        }
        Jedis conn = null;
        try {
            // 1.建立redis连接
            conn = jedisPool.getResource();
            if (conn.get(lockKey).equals(identifier)) {
                conn.del(lockKey);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

}
