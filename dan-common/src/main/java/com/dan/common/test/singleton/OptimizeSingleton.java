package com.dan.common.test.singleton;

import java.io.Serializable;


/**
 * @fileName: OptimizeSingleton
 * @author: Bo
 * @createDate: 2019/07/30 16:40:47.
 * @description: 使用静态内部单例模式-可以多线程使用
 */
public class OptimizeSingleton implements Serializable {
    /**
     * 构造器私有化
     */
    private OptimizeSingleton() {
    }

    /**
     * 静态私有内部类
     */
    private static class SingletonHolder {
        private static final OptimizeSingleton SINGLETON = new OptimizeSingleton();
    }

    /**
     * 公有获取单例对象的函数
     *
     * @return OptimizeSingleton
     */
    public static OptimizeSingleton getInstance() {
        return SingletonHolder.SINGLETON;
    }

    /**
     * 防止反序列化重新创建对象
     *
     * @return Object
     */
    private Object readResolve() {
        System.out.println("===readResolve===");
        return SingletonHolder.SINGLETON;
    }

}
