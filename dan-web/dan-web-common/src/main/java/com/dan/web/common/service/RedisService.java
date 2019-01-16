package com.dan.web.common.service;

/**
 * @fileName: RedisService
 * @author: Dan
 * @createDate: 2018-05-21 16:06.
 * @description: redis
 */
public interface RedisService {

    boolean lock(String key, String value);

    void unlock(String key, String value);

}
