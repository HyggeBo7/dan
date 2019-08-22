package com.dan.common.test.thread;

import com.dan.common.test.thread.base.AbstractMultiParallelThreadHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: MultiParallelThreadHandler
 * @createDate: 2019-08-21 16:42.
 * @description:
 */
public class MultiParallelThreadHandler extends AbstractMultiParallelThreadHandler {


    private int runSize;

    public MultiParallelThreadHandler(int runSize) {
        super();
        this.runSize = runSize < 0 ? Runtime.getRuntime().availableProcessors() : runSize;
    }

    /**
     * 根据任务数量运行任务
     */
    @Override
    public void run() {
        if (taskList == null || taskList.size() < 1) {
            return;
        }
        ExecutorService service = Executors.newFixedThreadPool(runSize);
        for (TaskRunnable runnable : taskList) {
            service.execute(runnable);
        }
        service.shutdown();
    }

    public int getAtomicIntegerCountSum() {
        int sunCount = 0;
        for (TaskRunnable taskRunnable : taskList) {
            sunCount += taskRunnable.getAtomicIntegerCount().get();
        }
        return sunCount;
    }

    public int getWorkNumCountSum() {
        int sunCount = 0;
        for (TaskRunnable taskRunnable : taskList) {
            sunCount += taskRunnable.getWorkNumCount();
        }
        return sunCount;
    }

    public int getTaskSum() {
        return taskList.size();
    }
}
