package top.dearbo.util.data;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author Bo
 * @ClassName: JsonJacksonUtils
 * @Description: Jackson工具类
 * @DateTime 2020年6月1日 上午8:42:36
 */
public class JacksonUtils {
    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
    /**
     * 默认
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 可以忽略null属性
     */
    private static final ObjectMapper MAPPER_NOT_NULL = new ObjectMapper();

    static {
        // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
        MAPPER.setSerializationInclusion(Include.ALWAYS)
                // 设置Date类型的序列化及反序列化格式
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // 忽略空Bean转json的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
                .registerModule(new JavaTimeModule());
        MAPPER_NOT_NULL.setSerializationInclusion(Include.NON_NULL)
                // 设置Date类型的序列化及反序列化格式
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // 忽略空Bean转json的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
                .registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static ObjectMapper getMapperNotNull() {
        return MAPPER_NOT_NULL;
    }

    /**
     * 将对象转换成json字符串
     *
     * @param data 对象
     * @return json字符串
     */
    public static String toJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error(" toJson error: ", e);
        }
        return null;
    }

    /**
     * 将对象转换成json字符串（忽略空值的key）
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String toJsonIgnoreNull(Object obj) {
        try {
            return MAPPER_NOT_NULL.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(" toJsonIgnoreNull error: ", e);
        }
        return null;
    }

    /**
     * 将json数据中指定节点的json数据转换为JavaBean
     *
     * @param jsonString json数据
     * @param treeNode   指定节点key
     * @param clazz      JavaBean
     * @return bean
     */
    public static <T> T jsonBeanByTree(String jsonString, String treeNode, Class<T> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonString);
            JsonNode data = jsonNode.findPath(treeNode);
            return jsonToBean(data.toString(), clazz);
        } catch (Exception e) {
            log.error(" jsonBeanByTree error: ", e);
        }
        return null;
    }

    /**
     * Json数据转换为map
     *
     * @param jsonString json数据
     * @return Map
     */
    public static <T> Map<String, T> toMap(String jsonString) {
        try {
            return MAPPER.readValue(jsonString, new TypeReference<Map<String, T>>() {
            });
        } catch (Exception e) {
            log.error(" toMap error: ", e);
        }
        return null;
    }

    /**
     * Json数据转换为list map
     *
     * @param jsonString json数据
     * @return mapList
     */
    public static <T> List<Map<String, T>> toMapList(String jsonString) {
        try {
            return MAPPER.readValue(jsonString, new TypeReference<List<Map<String, T>>>() {
            });
        } catch (Exception e) {
            log.error(" toMapList error: ", e);
        }
        return null;
    }

    /**
     * 将json数据转换为map（忽略空值的key）
     *
     * @param jsonString json数据
     * @return Map
     */
    public static <T> Map<String, T> toMapIgnoreNull(String jsonString) {
        try {
            return MAPPER_NOT_NULL.readValue(jsonString, new TypeReference<Map<String, T>>() {
            });
        } catch (Exception e) {
            log.error(" toMapIgnoreNull error: ", e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType JavaBean
     * @return bean
     */
    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error(" jsonToBean error: ", e);
        }
        return null;
    }

    /**
     * 将map转换为JavaBean
     *
     * @param map   map数据
     * @param clazz JavaBean
     * @return bean
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        try {
            return MAPPER.convertValue(map, clazz);
        } catch (Exception e) {
            log.error(" mapToBean error: ", e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param jsonData json数据
     * @param beanType JavaBean
     * @return bean集合
     */
    public static <T> List<T> jsonToBeanList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error(" jsonToBeanList error: ", e);
        }
        return null;
    }

    /**
     * 将指定节点的JSON数组转换为集合
     *
     * @param jsonStr  json数据
     * @param treeNode 节点key
     * @param clazz    JavaBean
     * @return bean集合
     */
    public static <T> List<T> jsonListByTree(String jsonStr, String treeNode, Class<T> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonStr);
            JsonNode data = jsonNode.findPath(treeNode);
            return jsonToBeanList(data.toString(), clazz);
        } catch (Exception e) {
            log.error(" jsonListByTree error: ", e);
        }
        return null;
    }

}