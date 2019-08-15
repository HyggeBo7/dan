package com.dan.utils.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * @fileName: ObjectUtil
 * @author: Dan
 * @createDate: 2019-04-11 9:02.
 * @description: 工具类
 */
public class ObjectUtil {

    /**
     * 判断Boolean是否为true
     */
    public static boolean booleanIsTrue(Boolean flag) {
        return flag != null && flag;
    }

    /**
     * 判断Boolean是否为false
     */
    public static boolean booleanIsFalse(Boolean flag) {
        return flag != null && !flag;
    }

    /**
     * 判断Boolean是否为Null
     */
    public static boolean booleanIsNull(Boolean flag) {
        return flag == null;
    }

    /**
     * String:1,2,3 转List<Integer> 集合
     *
     * @param str 字符串
     * @return List<Integer>
     */
    public static List<Integer> stringToIntegerList(String str) {
        return stringToIntegerList(str, ",");
    }

    /**
     * String:1,2,3 转List<Integer> 集合
     *
     * @param str       字符串
     * @param separator 分割符
     * @return List<Integer>
     */
    public static List<Integer> stringToIntegerList(String str, String separator) {
        List<Integer> integerList = new ArrayList<>();
        if (str != null && !"".equals(str.trim())) {
            String[] splitArr = str.split(separator);
            if (splitArr.length > 0) {
                for (String s : splitArr) {
                    integerList.add(Integer.valueOf(s));
                }
            }
        }
        return integerList;
    }

}
