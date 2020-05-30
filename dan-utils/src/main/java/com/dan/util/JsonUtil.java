package com.dan.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @fileName: JsonUtil
 * @author: Dan
 * @createDate: 2018-04-11 9:37.
 * @description: json工具类
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final Gson GSON = new GsonBuilder()
            //取消自动转义("=" === "\u003d")
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static final Gson GSON_SERIALIZE_NULL = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

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

    /**
     * 将json转为Map
     *
     * @param json json
     * @param <T>  value-泛型
     * @return Map<String, Object>
     */
    public static <T> Map<String, T> toMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, new TypeToken<LinkedHashMap<String, T>>() {
        }.getType());
    }

    /**
     * 将json转为Map List
     *
     * @param json json
     * @param <T>  泛型
     * @return List<T>
     */
    public static <T> List<Map<String, T>> toMapList(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json,
                new TypeToken<List<Map<String, T>>>() {
                }.getType());
    }

    /**
     * 转成Object int to double 问题
     *
     * @return List<TreeMap < String, Object>>
     */
    @Deprecated
    public static List<Map<String, Object>> jsonToMapList(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(
                        new TypeToken<TreeMap<String, Object>>() {
                        }.getType(),
                        new JsonDeserializer<TreeMap<String, Object>>() {
                            @Override
                            public TreeMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
                                TreeMap<String, Object> treeMap = new TreeMap<>();
                                if (json.isJsonObject()) {
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        if (entry.getValue().isJsonNull()) {
                                            treeMap.put(entry.getKey(), null);
                                        } else if (entry.getValue().isJsonPrimitive()) {
                                            treeMap.put(entry.getKey(), entry.getValue().getAsString());
                                        } else {
                                            treeMap.put(entry.getKey(), entry.getValue());
                                        }
                                    }
                                }
                                return treeMap;
                            }
                        }).create();

        List<Map<String, Object>> list = new ArrayList<>();

        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, new TypeToken<TreeMap<String, Object>>() {
            }.getType()));
        }
        return list;
    }

    @Deprecated
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(
                        new TypeToken<LinkedHashMap<String, Object>>() {
                        }.getType(),
                        new JsonDeserializer<LinkedHashMap<String, Object>>() {
                            @Override
                            public LinkedHashMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
                                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
                                if (json.isJsonObject()) {
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        if (entry.getValue().isJsonNull()) {
                                            linkedHashMap.put(entry.getKey(), null);
                                        } else if (entry.getValue().isJsonPrimitive()) {
                                            linkedHashMap.put(entry.getKey(), entry.getValue().getAsString());
                                        } else {
                                            linkedHashMap.put(entry.getKey(), entry.getValue());
                                        }
                                    }
                                }
                                return linkedHashMap;
                            }
                        }).create();

        JsonObject asJsonObject = JsonParser.parseString(json).getAsJsonObject();
        return gson.fromJson(asJsonObject, new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType());
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

    static ParameterizedType type(final Class<?> raw, final Type... args) {
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

}
