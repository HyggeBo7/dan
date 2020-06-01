package top.dearbo.frame.common.test.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: SingletonV1
 * @createDate: 2019-07-30 16:08.
 * @description: 单例-饿汉式
 */
public class SingletonV1 implements Serializable {

    private static SingletonV1 mInstance = new SingletonV1();

    private SingletonV1() {

    }

    public static SingletonV1 getInstance() {
        return mInstance;
    }

    /**
     * 防止反序列化
     * 由于反序列化时会调用readResolve这个钩子方法，只需要把当前的TestSingletonDCL对象返回而不是去创建一个新的对象
     *
     * @return Object
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return mInstance;
    }

}





