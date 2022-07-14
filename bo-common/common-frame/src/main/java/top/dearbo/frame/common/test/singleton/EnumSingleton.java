package top.dearbo.frame.common.test.singleton;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: SingletonEnum
 * @createDate: 2019-11-08 16:54.
 * @description: 单例-枚举方式
 */
public enum EnumSingleton {

    INSTANCE;

    public void testEnumSingleton() {
        System.out.println("testEnumSingleton");
    }
}
