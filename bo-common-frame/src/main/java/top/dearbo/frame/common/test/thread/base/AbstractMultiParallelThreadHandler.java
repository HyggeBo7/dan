package top.dearbo.frame.common.test.thread.base;

import top.dearbo.frame.common.test.thread.TaskRunnable;
import top.dearbo.frame.common.test.thread.impl.MultiThreadHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: AbstractMultiParallelThreadHandler
 * @createDate: 2019-08-21 16:39.
 * @description:
 */
public abstract class AbstractMultiParallelThreadHandler implements MultiThreadHandler {

    /**
     * 任务列表
     */
    protected List<TaskRunnable> taskList;

    public AbstractMultiParallelThreadHandler() {
        taskList = new ArrayList<>();
    }

    @Override
    public void addTask(TaskRunnable... tasks) {
        if (tasks != null && tasks.length > 0) {
            taskList.addAll(Arrays.asList(tasks));
        }
    }

    /**
     * 运行任务
     */
    @Override
    public abstract void run();
}
