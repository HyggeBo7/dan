package com.dan.common.test.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * @fileName: SingletonV2
 * @author: Bo
 * @createDate: 2019/07/30 16:40:54.
 * @description: 懒汉式-DCL单例模式-不推荐多线程使用
 */
public class SingletonV2 implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 静态成员私有化，注意使用volatile关键字，因为会存在DCL失效的问题,重排序,增加可见性
     */
    private static volatile SingletonV2 mInstance = null;

    public static SingletonV2 getInstance() {
        //双重检验锁-解决获取对象效率问题
        if (mInstance == null) {
            synchronized (SingletonV2.class) {
                if (mInstance == null) {
                    mInstance = new SingletonV2();
                }
            }
        }
        return mInstance;
    }

    /**
     * 防止反序列化,保证单例
     * 由于反序列化时会调用readResolve这个钩子方法，只需要把当前的SingletonV2对象返回而不是去创建一个新的对象
     *
     * @return Object
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return mInstance;
    }
}
