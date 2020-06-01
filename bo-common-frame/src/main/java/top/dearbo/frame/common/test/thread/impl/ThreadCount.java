package top.dearbo.frame.common.test.thread.impl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: MultiThreadCount
 * @createDate: 2019-08-21 16:17.
 * @description: 统计任务完成数量
 */
public interface ThreadCount {

    /**
     * 当前进度
     */
    AtomicInteger getAtomicIntegerCount();

    /**
     * 当前工作量
     */
    Integer getWorkNumCount();

}
