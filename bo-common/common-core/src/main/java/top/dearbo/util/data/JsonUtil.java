package top.dearbo.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @fileName: JsonUtil
 * @author: Bo
 * @createDate: 2018-04-11 9:37.
 * @description: json工具类
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final Gson GSON;

    public static final Gson GSON_SERIALIZE_NULL;

    public static final Gson GSON_MAP;

    static {
        GSON = new GsonBuilder()
                //取消自动转义("=" === "\u003d")
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        GSON_SERIALIZE_NULL = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        GSON_MAP = new GsonBuilder()
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new MapTypeAdapter()).create();
    }

    /**
     * 获取GsonBuilder实例
     *
     * @return GsonBuilder
     */
    public static GsonBuilder builder() {
        return new GsonBuilder();
    }

    public static <T> T fromJson(final String json, final Class<T> cls) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, cls);
    }

    public static <T> T fromTypeJson(final String json, final Type typeOfT) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成list
     *
     * @param json  json字符串
     * @param clazz 指定泛型类
     * @return 返回泛型
     */
    public static <T> List<T> fromListJson(final String json, final Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * 字符串转对象
     *
     * @param json      字符串
     * @param clazzType 转换的对象:Pagination.class <T>
     * @param clazz     泛型类型: User.cass
     * @param <T>       Pagination<User>
     * @return Pagination<User>
     */
    public static <T> T fromGenericJson(String json, Class<?> clazzType, Class<?> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Type objectType = type(clazzType, clazz);
        return GSON.fromJson(json, objectType);
    }

    /**
     * 将json转为Map
     *
     * @param json json
     * @return Map<String, Object>
     */
    public static Map<String, Object> toMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON_MAP.fromJson(json, new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType());
    }

    /**
     * 将json转为Map List
     * 解决整数为浮点数
     *
     * @param json json
     * @return List<T>
     */
    public static List<Map<String, Object>> toMapList(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON_MAP.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    /**
     * 将object对象转成json字符串
     *
     * @param obj 对象
     * @return json String
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return GSON.toJson(obj);
    }

    public static String toJsonSerializeNull(Object obj) {
        if (obj == null) {
            return null;
        }
        return GSON_SERIALIZE_NULL.toJson(obj);
    }

    public static ParameterizedType type(final Class<?> raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();
                case NUMBER:
                    //将其作为一个字符串读取出来
                    String numberStr = in.nextString();
                    //返回的numberStr不会为null
                    if (numberStr.contains("e") || numberStr.contains("E")) {
                        return Double.parseDouble(numberStr);
                    }
                    if (numberStr.contains(".")) {
                        BigDecimal bigDecimal = new BigDecimal(numberStr);
                        if (numberStr.endsWith(".0")) {
                            return bigDecimal.longValue();
                        }
                        return bigDecimal.doubleValue();
                    }
                    return Long.parseLong(numberStr);
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }

}

