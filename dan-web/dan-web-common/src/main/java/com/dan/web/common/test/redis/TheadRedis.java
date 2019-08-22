package com.dan.web.common.test.redis;

import com.dan.web.common.test.redis.service.impl.LockRedisService;
import com.dan.web.common.util.RedisUtils;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: TheadRedis
 * @createDate: 2019-08-22 11:00.
 * @description:
 */
public class TheadRedis extends Thread {

    private String name;

    public TheadRedis(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        secKill();
    }

    public void secKill() {
        LockRedisService lockRedisService = new LockRedisService(RedisUtils.getRedisPool());
        String identifier = lockRedisService.getRedisLock(5000L, 10000L);
        if (identifier == null) {
            System.out.println(Thread.currentThread().getName() + ",获取锁失败,获取锁超时...");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ",获取锁成功,identifier:【" + identifier + "】");
        //释放锁
        boolean unRedisLockFlag = lockRedisService.unRedisLock(identifier);
        if (unRedisLockFlag) {
            System.out.println(Thread.currentThread().getName() + ",释放锁成功,identifier:【" + identifier + "】");
        } else {
            System.out.println(Thread.currentThread().getName() + ",释放锁失败,identifier:【" + identifier + "】");
        }
    }

}
