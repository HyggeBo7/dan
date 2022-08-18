package top.dearbo.frame.common.test.thread;

import top.dearbo.util.lang.DateUtil;
import top.dearbo.util.lang.RandomUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: MyThread
 * @createDate: 2019-08-13 15:52.
 * @description:
 */
public class MyThread implements Runnable {
    private AtomicInteger onlineCount;
    private String name;

    public MyThread(AtomicInteger onlineCount, String name) {
        this.onlineCount = onlineCount;
        this.name = name;
    }

    public int getOnlineCount() {
        return onlineCount.get();
    }

    @Override
    public void run() {
        System.out.println("开始执行===>【" + DateUtil.parseToString(new Date()) + "】===>:name:" + name + ",当前线程完成数量:【" + getOnlineCount() + "】");
        try {
            Thread.sleep(RandomUtil.getRandomInt(3000, 8000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行结束===>【" + DateUtil.parseToString(new Date()) + "】===>:name:" + name + ",当前线程完成数量:【" + onlineCount.incrementAndGet() + "】");
    }
}
