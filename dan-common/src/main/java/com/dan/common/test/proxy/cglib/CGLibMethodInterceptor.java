package com.dan.common.test.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: CGLibMethodInterceptor
 * @createDate: 2019-11-12 16:55.
 * @description: cglib 拦截器
 */
public class CGLibMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("-CGLIB->>>打印订单日志开始" + obj.getClass().toString());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("-CGLIB->>>打印订单日志结束");
        return result;
    }
}
