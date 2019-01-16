package com.dan.web.common.service.impl;

import com.dan.utils.exception.AppException;
import com.dan.web.common.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @fileName: RedisServiceImpl
 * @author: Dan
 * @createDate: 2018-05-21 15:59.
 * @description: redis
 */
@Service
public class RedisServiceImpl implements RedisService {

    /**
     * 10秒
     */
    private static final int TIME_OUT = 10 * 1000;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param value 当前时间 + 超时时间
     * @return
     */
    @Override
    public boolean lock(String key, String value) {
        // 加锁
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        // 只会是一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);

        // 如果当前锁过期
        if (StringUtils.isNotBlank(currentValue) && Long.parseLong(value) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.isNotBlank(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    @Override
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            System.err.println("【redis分布式锁加锁异常】：" + e);
        }
    }

    public static void main(String[] args) {
        String key = "dan";
        RedisServiceImpl redisUtil = new RedisServiceImpl();
        //加锁
        String strTime = String.valueOf(System.currentTimeMillis() + TIME_OUT);
        //如果加锁失败.
        if (!redisUtil.lock(key, strTime)) {
            AppException.throwEx("人太多了，抢购失败!");
        }
        //逻辑代码

        //解锁
        redisUtil.unlock(key, strTime);
    }

}
