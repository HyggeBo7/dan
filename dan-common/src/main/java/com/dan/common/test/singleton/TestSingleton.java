package com.dan.common.test.singleton;

import com.dan.util.JsonUtil;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: TestSingleton
 * @createDate: 2019-11-08 16:18.
 * @description:
 */
public class TestSingleton {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        //1.
        OptimizeSingleton optimizeSingleton1 = OptimizeSingleton.getInstance();
        //获取类的无参构造函数
        Constructor<OptimizeSingleton> declaredConstructor = OptimizeSingleton.class.getDeclaredConstructor();
        //设置访问权限
        declaredConstructor.setAccessible(true);
        //新创建一个
        OptimizeSingleton optimizeSingleton2 = declaredConstructor.newInstance();
        System.out.println(optimizeSingleton1 == optimizeSingleton2);

        //2.
        SingletonV2 singletonV21 = SingletonV2.getInstance();
        singletonV21.setName("SingletonDCL1");
        //反射破解单例
        Constructor<SingletonV2> singletonDCLConstructor = SingletonV2.class.getDeclaredConstructor();
        singletonDCLConstructor.setAccessible(true);
        SingletonV2 singletonV22 = singletonDCLConstructor.newInstance();
        System.out.println(singletonV21 == singletonV22);

        singletonV22.setName("SingletonDCL2");
        //序列化json破解单例
        String toJson = JsonUtil.toJson(singletonV22);
        SingletonV2 singletonV2Json = JsonUtil.fromJson(toJson, SingletonV2.class);
        System.out.println("singletonDCLJson:" + (singletonV2Json == singletonV22));

        //序列化破解单例
        //序列化 把对象从内存写入硬盘
        String path = "P:/file/code/SingletonV2.obj";
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(singletonV21);
        oos.flush();
        oos.close();
        //反序列化-从硬盘把数据读取到内存中
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        SingletonV2 newSingletonV21 = (SingletonV2) ois.readObject();
        ois.close();
        System.out.println(singletonV21 == newSingletonV21);

        //3.枚举-反射枚举会直接报错,枚举禁止反射(jdk底层有验证)
        EnumSingleton enumSingleton = EnumSingleton.INSTANCE;
        enumSingleton.testEnumSingleton();
    }
}
