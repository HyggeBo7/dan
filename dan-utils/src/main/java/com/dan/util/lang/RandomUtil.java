package com.dan.util.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @fileName: RandomUtil
 * @author: Dan
 * @createDate: 2018-12-27 9:49.
 * @description: 随机数
 */
public class RandomUtil {

    private static Random rand;

    static {
        rand = new Random();
    }

    private RandomUtil() {

    }

    public static String uuidNum() {
        return uuidNum("");
    }

    public static String uuidNum(String prefix) {
        prefix = prefix == null ? "" : prefix;
        //最大支持1-9个集群机器部署
        int machineId = getRandomInt(1, 9);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        int length = getRandomInt(8, 11);
        return prefix + machineId + String.format("%0" + length + "d", hashCodeV);
    }


    /**
     * 随机获取一个小写字母
     */
    public static char getLetter() {
        return (char) getRandomInt(97, 122);
    }

    /**
     * 随机获取一个大写字母
     */
    public static char getUpper() {
        return (char) getRandomInt(65, 90);
    }

    /**
     * 随机获取一个0-9的数字
     *
     * @return
     */
    public static int getNum() {
        return getRandomInt(0, 9);
    }

    /**
     * 获取一个范围内的随机数字
     */
    public static int getRandomInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static String UUID() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static String nextSeq() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyMMddHHmmss.SS") + org.apache.commons.lang3.RandomUtils.nextLong(100000000L, 999999999L);
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
