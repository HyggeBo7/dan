package com.dan.web.common.test.redis.service.impl;

/**
 * @fileName: RedisService
 * @author: Dan
 * @createDate: 2018-05-21 16:06.
 * @description: redis
 */
public interface RedisService {

    boolean lock(String key, String value);

    boolean unlock(String key, String value);

}

