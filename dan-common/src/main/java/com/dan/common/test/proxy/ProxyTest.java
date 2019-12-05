package com.dan.common.test.proxy;

import com.dan.common.test.proxy.cglib.CGLibMethodInterceptor;
import com.dan.common.test.proxy.jdk.JdkInvocationHandler;
import com.dan.common.test.proxy.service.OrderService;
import com.dan.common.test.proxy.service.impl.OrderServiceImpl;
import net.sf.cglib.proxy.Enhancer;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ProxyTest
 * @createDate: 2019-11-12 11:51.
 * @description: 静态代理和动态代理测试类
 * 1.静态代理
 * -静态代理人为创建好代理类,静态代理实现方式(继承,实现接口)
 * 2.动态代理
 * -1).通过，Java反射或者字节码技术自动的帮助我们在运行的时候创建这个代理类对象
 */
public class ProxyTest {

    public static void main(String[] args) {
        /// 1.使用jdk动态代理-测试方法
        //开启-获取jdk动态代理生成的class文件(必须在main方法里面执行)
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //JDK动态生成的代理类都是通过实现接口的形式,所以代理类必须实现接口,代理类也实现了OrderService,所以可以使用OrderService接收 PS:(只能代理实现接口的类,没有实现接口的类不能动态代理)
        OrderService orderServiceProxy = new JdkInvocationHandler(new OrderServiceImpl()).getProxy();
        orderServiceProxy.executeOrder();
        orderServiceProxy.testOrder("哈哈哈--->testOrder");
        /// 2.使用cglib动态代理 PS:(被代理可以不用实现OrderService)
        /*CGLibMethodInterceptor cgLibMethodInterceptor = new CGLibMethodInterceptor();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OrderServiceImpl.class);
        enhancer.setCallback(cgLibMethodInterceptor);
        OrderService orderServiceProxy = (OrderServiceImpl) enhancer.create();
        orderServiceProxy.executeOrder();*/
    }

}
