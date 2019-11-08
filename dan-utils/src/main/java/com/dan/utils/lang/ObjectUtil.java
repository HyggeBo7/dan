package com.dan.utils.lang;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @fileName: ObjectUtil
 * @author: Dan
 * @createDate: 2019-04-11 9:02.
 * @description: 工具类
 */
public class ObjectUtil {

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * <pre>
     * ObjectUtils.isEmpty(null)             = false
     * ObjectUtils.isEmpty("")               = false
     * ObjectUtils.isEmpty(new int[]{})      = false
     * ObjectUtils.isEmpty("ab")             = true
     * ObjectUtils.isEmpty(new int[]{1,2,3}) = true
     * ObjectUtils.isEmpty(1234)             = true
     * </pre>
     *
     * @param obj 值
     * @return true/false
     */
    public static boolean isNotEmptyObj(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() > 0;
        } else if (obj instanceof List) {
            return ((List) obj).size() > 0;
        } else if (obj instanceof Map) {
            return ((Map) obj).size() > 0;
        } else if (obj instanceof Set) {
            return ((Set) obj).size() > 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size() > 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) > 0;
        }
        return !obj.toString().isEmpty();
    }

    public static boolean isEmptyObj(Object obj) {

        return !isNotEmptyObj(obj);
    }

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
        return stringToObjectList(str, separator, strValue -> Integer.valueOf(strValue));
    }

    public static <T> List<T> stringToObjectList(String str, ITypeConversion<T> typeConversion) {

        return stringToObjectList(str, ",", typeConversion);
    }

    public static <T> List<T> stringToObjectList(String str, String separator, ITypeConversion<T> typeConversion) {
        List<T> objectList = new ArrayList<>();
        if (str != null && !"".equals(str.trim())) {
            String[] splitArr = str.split(separator);
            if (splitArr.length > 0) {
                for (String s : splitArr) {
                    T value = typeConversion.getValue(s);
                    if (value == null && typeConversion.excludeEmpty()) {
                        continue;
                    }
                    objectList.add(value);
                }
            }
        }
        return objectList;
    }

    public interface ITypeConversion<T> {

        /**
         * String转换指定Value
         *
         * @param str string 值
         * @return 转换后的值
         */
        T getValue(String str);

        /**
         * 是否排除空-默认true:排除
         *
         * @return boolean
         */
        default boolean excludeEmpty() {
            return true;
        }

    }

}
