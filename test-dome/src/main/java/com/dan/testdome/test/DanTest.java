package com.dan.testdome.test;

import com.dan.utils.DateUtil;
import com.dan.utils.HideDataUtil;
import com.dan.utils.JsonUtil;
import com.dan.utils.file.FileUtil;
import com.dan.utils.xt.PinYinUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @fileName: DanTest
 * @author: Dan
 * @createDate: 2018-05-21 16:11.
 * @description:
 */
public class DanTest {

    @Test
    public void test01() {
        int dayOfMonth = DateUtil.getDayOfMonth(2018, 6);
        System.out.println("dayOfMonth:" + dayOfMonth);
    }

    @Test
    public void test02() {
        Date startOfDay = DateUtil.getEndOfDay(new Date());
        System.out.println("startOfDay:" + DateUtil.parseToString(startOfDay));
    }

    @Test
    public void test03() {
        String s1 = "2018-09-10 22:00:00";
        String s2 = "2018-09-10 10:20:30";
        Date date1 = DateUtil.parseToDate(s1, DateUtil.FORMAT_LONG);
        Date date2 = DateUtil.parseToDate(s2, DateUtil.FORMAT_LONG);

        Boolean aBoolean = DateUtil.equalsDate(s1, s2);
        System.out.println("equalsDate:" + aBoolean);
    }

    @Test
    public void test04() {
        String fileUrl = "A:/opencv_img/success";
        List<String> stringList = FileUtil.readFileAndPath(fileUrl, false);
        System.out.println("test04():" + JsonUtil.toJson(stringList));
    }

    @Test
    public void test05() {

        String filePath = "A:/opencv_img/success1/rrr.rde";
        File file = new File(filePath);
        //不存在
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("创建文件夹." + mkdirs);
        } else {
            System.out.println("存在了.");
        }

    }

    @Test
    public void test06() {

        String filePath = "P:\\file/";
        String endPath = "/dan/dan1";
        String fileUrl = FileUtil.createMkdirMulti(filePath, true, endPath, "aaaa", "/dassfasd/werqwe", "sdafa\\dawerq/");
        System.out.println("fileUrl:" + fileUrl);

    }

    @Test
    public void test07() {
        String url = "BO1/上海梅龙镇广场有限公司/dddddd/";
        //获取第N次字符串出现的位置
        int i = StringUtils.ordinalIndexOf(url, "/", 2);
        System.out.println("ordinalIndexOf:" + i);
    }

    @Test
    public void test08() {
        String idCar = "51130319980625";
        String s = HideDataUtil.hideAllIdCardNum(idCar, 1, 1);
        System.out.println("s:" + s);

    }

    @Test
    public void testPinYin() {
        System.out.println(PinYinUtil.getPinYin("吴波"));
    }

}
