package com.dan.utils.lang;

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

}
