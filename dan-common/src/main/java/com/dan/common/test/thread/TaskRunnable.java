package com.dan.common.test.thread;

import com.dan.common.test.thread.impl.ThreadCount;
import com.dan.util.lang.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: TaskRunnable
 * @createDate: 2019-08-21 16:24.
 * @description: 执行的任务-执行逻辑
 */
public class TaskRunnable implements Runnable, ThreadCount {

    private static final Logger logger = LoggerFactory.getLogger(TaskRunnable.class);

    private AtomicInteger atomicInteger;
    private String name;
    private Integer workNum;

    public TaskRunnable(String name, Integer workNum) {
        this.atomicInteger = new AtomicInteger(0);
        this.name = name;
        this.workNum = workNum;

    }

    @Override
    public AtomicInteger getAtomicIntegerCount() {
        return atomicInteger;
    }

    @Override
    public Integer getWorkNumCount() {
        return workNum;
    }

    @Override
    public void run() {
        for (int i = 1; i <= workNum; i++) {
            logger.info("任务:【{}】,完成度:【{}/{}】", name, i, workNum);
            atomicInteger.incrementAndGet();
            try {
                Thread.sleep(RandomUtil.getRandomInt(800, 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
