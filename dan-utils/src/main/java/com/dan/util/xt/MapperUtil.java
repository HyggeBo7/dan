package com.dan.util.xt;

import com.dan.util.exception.AppException;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

/**
 * @fileName: MapperUtil
 * @author: Dan
 * @createDate: 2018-12-05 11:20.
 * @description: Mapper工具类
 */
public class MapperUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapperUtil.class);

    /**
     * 使用多线程安全的Map来缓存BeanCopier，由于读操作远大于写，所以性能影响可以忽略
     */
    public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 通过cglib BeanCopier形式
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier = null;
        copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        // putIfAbsent已经实现原子操作了。
        beanCopierMap.putIfAbsent(beanKey, copier);
        copier = beanCopierMap.get(beanKey);
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    /**
     * ×通过常规反射形式
     * DTO对象转换为实体对象。如命名不规范或其他原因导致失败。
     *
     * @param t 源转换的对象
     * @param e 目标转换的对象
     */
    public static <T, E> void transalte(T t, E e) {
        Method[] tms = t.getClass().getDeclaredMethods();
        Method[] tes = e.getClass().getDeclaredMethods();
        for (Method m1 : tms) {
            if (m1.getName().startsWith("get")) {
                String mNameSubfix = m1.getName().substring(3);
                String forName = "set" + mNameSubfix;
                for (Method m2 : tes) {
                    if (m2.getName().equals(forName)) {
                        // 如果类型一致，或者m2的参数类型是m1的返回类型的父类或接口
                        boolean canContinue = m2.getParameterTypes()[0].isAssignableFrom(m1.getReturnType());
                        if (canContinue) {
                            try {
                                m2.invoke(e, m1.invoke(t));
                                break;
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 用于将一个列表转换为列表中的对象的某个属性映射到列表中的对象
     *
     * <pre>
     *      List<UserDTO> userList = userService.queryUsers();
     *      Map<Integer, userDTO> userIdToUser = BeanUtil.mapByKey("userId", userList);
     * </pre>
     *
     * @param key 属性名
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapByKey(String key, List<? extends Object> list) {
        Map<K, V> map = new HashMap<K, V>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        try {
            Class<? extends Object> clazz = list.get(0).getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) {
                throw new IllegalArgumentException("Could not find the key");
            }
            field.setAccessible(true);
            for (Object o : list) {
                map.put((K) field.get(o), (V) o);
            }
        } catch (Exception e) {
            AppException.throwEx(e);
        }
        return map;
    }

    /**
     * 根据列表里面的属性聚合
     *
     * <pre>
     *       List<ShopDTO> shopList = shopService.queryShops();
     *       Map<Integer, List<ShopDTO>> city2Shops = BeanUtil.aggByKeyToList("cityId", shopList);
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> aggByKeyToList(String key, List<? extends Object> list) {
        Map<K, List<V>> map = new HashMap<K, List<V>>();
        // 防止外面传入空list
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        try {
            Class<? extends Object> clazz = list.get(0).getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) {
                throw new IllegalArgumentException("Could not find the key");
            }
            field.setAccessible(true);
            for (Object o : list) {
                K k = (K) field.get(o);
                if (map.get(k) == null) {
                    map.put(k, new ArrayList<V>());
                }
                map.get(k).add((V) o);
            }
        } catch (Exception e) {
            AppException.throwEx(e);
        }
        return map;
    }

    /**
     * 用于将一个对象的列表转换为列表中对象的属性集合
     *
     * <pre>
     *     List<UserDTO> userList = userService.queryUsers();
     *     Set<Integer> userIds = BeanUtil.toPropertySet("userId", userList);
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <K> Set<K> toPropertySet(String key, List<? extends Object> list) {
        Set<K> set = new HashSet<K>();
        // 防止外面传入空list
        if (CollectionUtils.isEmpty(list)) {
            return set;
        }
        try {
            Class<? extends Object> clazz = list.get(0).getClass();
            Field field = deepFindField(clazz, key);
            if (field == null) throw new IllegalArgumentException("Could not find the key");
            field.setAccessible(true);
            for (Object o : list) {
                set.add((K) field.get(o));
            }
        } catch (Exception e) {
            AppException.throwEx(e);
        }
        return set;
    }


    private static Field deepFindField(Class<? extends Object> clazz, String key) {
        Field field = null;
        while (!clazz.getName().equals(Object.class.getName())) {
            try {
                field = clazz.getDeclaredField(key);
                if (field != null) {
                    break;
                }
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    /**
     * 获取某个对象的某个属性
     */
    public static Object getProperty(Object obj, String fieldName) {
        try {
            Field field = deepFindField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            AppException.throwEx(e);
        }
        return null;
    }

    /**
     * 设置某个对象的某个属性
     */
    public static void setProperty(Object obj, String fieldName, Object value) {
        try {
            Field field = deepFindField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            AppException.throwEx(e);
        }
    }

    /**
     * Map转成实体对象
     *
     * @param mapValue  map实体对象包含属性
     * @param classType 实体对象类型 Class.class
     * @param <T>       泛型
     * @return 泛型 实体类
     */
    public static <T> T mapToBean(Map<String, Object> mapValue, Class<T> classType) {

        return mapToBean(mapValue, classType, null);
    }

    /**
     * Map转成实体对象
     *
     * @param mapValue  map实体对象包含属性
     * @param classType 实体对象类型 Class.class
     * @param nullValue 如果值为null,设置默认值
     * @return 泛型 实体类
     */
    public static <T> T mapToBean(Map<String, Object> mapValue, Class<T> classType, Object nullValue) {
        if (mapValue == null) {
            return null;
        }
        // 获取类属性
        BeanInfo beanInfo = null;
        // 创建 JavaBean 对象
        T obj = null;
        try {
            beanInfo = Introspector.getBeanInfo(classType);
            obj = classType.newInstance();
        } catch (IntrospectionException | IllegalAccessException | InstantiationException e) {
            logger.info("==mapToBean-error:【{}】", e.getMessage(), e);
            return obj;
        }
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            Object value = mapValue.getOrDefault(propertyName, null);
            if (value == null && nullValue != null) {
                value = nullValue;
            }
            if (value != null) {
                try {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    descriptor.getWriteMethod().invoke(obj, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.info("==mapToBean-Method:【{}】,value:【{}】,error:【{}】", propertyName, value, e.getMessage(), e);
                }
            }
        }
        return obj;
    }

    /**
     * 将一个JavaBean对象转化为一个Map -如果属性为null不添加进Map设置
     *
     * @param obj 对象
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object obj) {
        return beanToMap(obj, false);
    }


    /**
     * 将一个JavaBean对象转化为一个Map
     *
     * @param bean   对象
     * @param sNulls 如果属性为null是否设置
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean, boolean sNulls) {
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
        if (bean == null) {
            return returnMap;
        }
        Class type = bean.getClass();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            logger.info("==beanToMap:【{}】", e.getMessage(), e);
            return returnMap;
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(bean);
                    if (!sNulls && result == null) {
                        continue;
                    }
                    returnMap.put(propertyName, result);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.info("==objectToMap-Method【{}】,result:【{}】,error:【{}】", propertyName, result, e.getMessage(), e);
                }
            }
        }
        return returnMap;
    }

    public static Map<String, Object> bean2ToMap(Object obj) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        if (obj == null) {
            return returnMap;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                returnMap.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException e) {
            logger.info("==objectToMap:【{}】,{}", e.getMessage(), e);
        }
        return returnMap;
    }

    /**
     * xml字符串转化为Map<String,String>对象
     *
     * @param xmlStr xml字符串
     * @return Map 对象
     */
    public static Map<String, Object> xmlToMap(String xmlStr) {
        Map<String, Object> retMap = new HashMap<String, Object>(16);
        Matcher m = ValidateUtils.XML_TAG.matcher(xmlStr.replace("<xml>", "").replace("</xml>", ""));
        while (m.find()) {
            retMap.put(m.group(1), m.group(2).replace("<![CDATA[", "").replace("]]>", ""));
        }
        return retMap;
    }

    /**
     * 将Map<String, Object>对象转化为xml字符串
     *
     * @param map Map<String,String>对象
     * @return xml字符串
     */
    public static String mapToXml(Map<String, String> map) {
        StringBuilder xmlStr = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (null != value && !"".equals(value)) {
                xmlStr.append(String.format("<%s><![CDATA[%s]]></%s>", key, value, key));
            }
        }
        xmlStr.append("</xml>");
        return xmlStr.toString();
    }
}