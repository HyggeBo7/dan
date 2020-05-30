package com.dan.util.mybatis;

import com.dan.util.exception.AppException;
import com.dan.util.lang.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @fileName: QueryCondition
 * @author: Dan
 * @createDate: 2019-05-06 9:39.
 * @description: mybatis查询条件构建
 */
public class MyBatisQueryCondition implements Serializable {

    /**
     * EQ类型
     */
    private final static String[] QUERY_EQUAL_TO_TYPE_ARR = new String[]{"class java.lang.Integer", "class java.lang.Boolean", "class java.math.BigDecimal", "class java.lang.Long", "class java.lang.Double", "class java.lang.Float"};
    /**
     * Like 类型
     */
    private final static String[] QUERY_LIKE_TYPE_ARR = new String[]{"class java.lang.String"};

    private final static String EQUAL_TO = "EqualTo";
    private final static String LIKE = "Like";
    private final static String AND = "and";

    /**
     * 设置查询条件,eq/like
     *
     * @param criteria 条件构造器
     * @param entity   实体类
     */
    public static void queryConditionEqualToOrLike(Object criteria, Object entity) {
        if (Objects.isNull(entity)) {
            return;
        }
        Class<?> cClass = criteria.getClass();
        Class<?> mClass = entity.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] declaredFields = mClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //判断是不是静态方法
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            //判断该属性是否能够访问
            boolean flag = field.isAccessible();
            if (!flag) {
                //设置能够访问
                field.setAccessible(true);
            }
            //获取值
            Object value = null;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                AppException.throwEx("获取值错误:" + e.getMessage());
            }
            if (!flag) {
                field.setAccessible(false);
            }
            if (Objects.isNull(value) || StringUtils.isBlank(String.valueOf(value))) {
                continue;
            }
            //criteria.addCriterion(StringUtil.firstToUpperCase(field.getName()) + " =", value, field.getName());

            //获取方法名称,首字母大写
            String methodName = StringUtil.firstToUpperCase(field.getName());
            // 获取属性的类型
            String type = field.getGenericType().toString();
            try {
                //判断该数组是否包含
                if (ArrayUtils.contains(QUERY_EQUAL_TO_TYPE_ARR, type)) {
                    //EqualTo
                    methodName = AND + methodName + EQUAL_TO;
                } else if (ArrayUtils.contains(QUERY_LIKE_TYPE_ARR, type)) {
                    //Like
                    methodName = AND + methodName + LIKE;
                } else {
                    //默认抛异常
                    AppException.throwEx("不支持的类型:【" + type + "】");
                }
                Method method = cClass.getMethod(methodName, (Class<?>) field.getGenericType());
                //Method method = cClass.getMethod(methodName, BigDecimal.class);
                flag = method.isAccessible();
                if (!flag) {
                    method.setAccessible(true);
                }
                method.invoke(criteria, value);
            } catch (NoSuchMethodException e) {
                AppException.throwEx("获取方法名称错误:【" + methodName + "】" + e.getMessage());
            } catch (IllegalAccessException | InvocationTargetException e) {
                AppException.throwEx("执行错误:【" + methodName + "】" + e.getMessage());
            }
        }
    }
}
