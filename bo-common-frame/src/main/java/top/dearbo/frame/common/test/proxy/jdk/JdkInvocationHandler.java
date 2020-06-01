package top.dearbo.frame.common.test.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: JdkInvocationHandler
 * @createDate: 2019-11-12 11:48.
 * @description: JDK动态代理类
 */
public class JdkInvocationHandler implements InvocationHandler {

    /**
     * 被代理类对象,目标代理对象
     */
    private Object targetProxy;

    public JdkInvocationHandler(Object targetProxy) {
        this.targetProxy = targetProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //proxy 是jdk生成的代理类
        System.out.println("-JDK->>>打印订单日志开始" + proxy.getClass().toString());
        //java机制执行方法,执行目标对象的方法
        Object result = method.invoke(targetProxy, args);
        System.out.println("-JDK->>>打印订单日志结束");
        return result;
    }

    /**
     * 使用jdk动态创建代理类
     *
     * @param <T> 被代理类对象
     * @return 被代理类对象
     */
    public <T> T getProxy() {

        return (T) Proxy.newProxyInstance(targetProxy.getClass().getClassLoader(), targetProxy.getClass().getInterfaces(), this);
    }

}
