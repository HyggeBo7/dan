package top.dearbo.frame.common.test.thread;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ThreadTest
 * @createDate: 2019-08-13 15:54.
 * @description:
 */
public class ThreadTest {

    public static void main(String[] args) {

        //final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
        /*
        List<String> stringList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            stringList.add("名称:" + i);
        }
        for (String name : stringList) {
            MyThread myThread = new MyThread(ONLINE_COUNT, name);
            Thread thread = new Thread(myThread);
            thread.start();
        }
        new Thread() {
            @Override
            public void run() {
                while (ONLINE_COUNT.get() < stringList.size()) {
                    System.out.println("当前完成数量:" + ONLINE_COUNT.get());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("结束.............当前完成数量:" + ONLINE_COUNT.get());
            }
        }.start();*/
        MultiParallelThreadHandler multiParallelThreadHandler = new MultiParallelThreadHandler(8);
        for (int i = 1; i <= 10; i++) {
            multiParallelThreadHandler.addTask(new TaskRunnable("任务" + i, i * 10));
        }
        //multiParallelThreadHandler.addTask(new TaskRunnable("任务01", 50));
        //multiParallelThreadHandler.addTask(new TaskRunnable("任务02", 30));
        //multiParallelThreadHandler.addTask(new TaskRunnable("任务03", 20));
        multiParallelThreadHandler.run();

        new Thread() {
            @Override
            public void run() {
                while (multiParallelThreadHandler.getAtomicIntegerCountSum() < multiParallelThreadHandler.getWorkNumCountSum()) {
                    System.out.println("当前完成数量:【" + multiParallelThreadHandler.getAtomicIntegerCountSum() + "/" + multiParallelThreadHandler.getWorkNumCountSum() + "】,线程数量:" + multiParallelThreadHandler.getTaskSum());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("结束===>当前完成数量:【" + multiParallelThreadHandler.getAtomicIntegerCountSum() + "/" + multiParallelThreadHandler.getWorkNumCountSum() + "】,线程数量:" + multiParallelThreadHandler.getTaskSum());
            }
        }.start();
    }
}
