package top.dearbo.frame.common.test.proxy.service;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: OrderService
 * @createDate: 2019-11-12 11:49.
 * @description: 被代理类接口
 */
public interface OrderService {

    /**
     * 共同抽象的方法
     */
    void executeOrder();
    void testOrder(String name);

}
