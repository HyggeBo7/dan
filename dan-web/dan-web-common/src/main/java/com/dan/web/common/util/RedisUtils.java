package com.dan.web.common.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: RedisUtils
 * @createDate: 2019-08-22 09:51.
 * @description:
 */
public class RedisUtils {

    /*private RedisUtils() {

    }*/

    /**
     * Redis服务器IP
     */
    private static String HOST = "192.168.2.111";
    /**
     * Redis的端口号
     */
    private static int PORT = 6379;
    /**
     * 访问密码
     */
    private static String AUTH = null;

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);

        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);

        //在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, HOST, PORT, 3000);
    }

    public static JedisPool getRedisPool() {

        return jedisPool;
    }

}
