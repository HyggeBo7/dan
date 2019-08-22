package com.dan.common.test.thread.impl;

import com.dan.common.test.thread.TaskRunnable;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: MultiThreadHandler
 * @createDate: 2019-08-21 16:16.
 * @description:
 */
public interface MultiThreadHandler {

    /**
     * 添加任务
     *
     * @param tasks
     */
    void addTask(TaskRunnable... tasks);

    /**
     * 执行任务
     */
    void run();

}
