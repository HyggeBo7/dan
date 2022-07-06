package top.dearbo.testdome.set.test;

import top.dearbo.util.lang.DateUtil;
import top.dearbo.testdome.set.ext.ExtLinkedList;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;

/**
 * @fileName: TestSet
 * @author: bo
 * @createDate: 2018-07-09 13:17.
 * @description: 测试集合
 */
public class TestSet {

    @Test
    public void test01() {
        int size = 100000;
        Date startDate = DateUtil.getDate();
        LinkedList<String> stringLinkedList = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            stringLinkedList.add("LinkedList:" + i);
        }
        for (int i = 0; i < stringLinkedList.size(); i++) {
            String s = stringLinkedList.get(i);
            System.out.println(s);
        }
        Date endDate = DateUtil.getDate();


        ExtLinkedList<String> extLinkedList = new ExtLinkedList<>();
        Date startDate2 = DateUtil.getDate();
        for (int i = 1; i <= size; i++) {
            extLinkedList.add("ExtLinkedList:" + i);
        }
        for (int i = 0; i < extLinkedList.size(); i++) {
            String s = extLinkedList.get(i);
            System.out.println(s);
        }
        Date endDate2 = DateUtil.getDate();
        System.out.println("LinkedList-时间：" + DateUtil.getMistiming(startDate, endDate) + ",大小：" + stringLinkedList.size());

        System.out.println("ExtLinkedList-时间：" + DateUtil.getMistiming(startDate2, endDate2) + ",大小：" + stringLinkedList.size());

    }

}
