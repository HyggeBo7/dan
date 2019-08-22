package com.dan.web.common.test;


import com.dan.web.common.test.redis.TheadRedis;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: DanTest
 * @createDate: 2019-08-22 10:35.
 * @description:
 */
public class DanTest {

    public static void main(String[] args) {
        for (int i = 1; i <= 1000; i++) {
            new TheadRedis("任务:" + i).start();
        }
        System.out.println("========");
    }


}
