package top.dearbo.frame.common.test.proxy.service.impl;

import top.dearbo.frame.common.test.proxy.service.OrderService;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: OrderService
 * @createDate: 2019-11-12 11:49.
 * @description: 被代理类接口
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public void executeOrder() {
        System.out.println("执行订单逻辑操作...");
    }

    @Override
    public void testOrder(String name) {
        System.out.println("执行测试订单操作..." + name);
    }
}
