package com.dan.utils;

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

    private static final Gson GSON = new GsonBuilder()
            //取消自动转义("=" === "\u003d")
            .disableHtmlEscaping()
//            .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//            .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();


    public static Gson getGson() {
        return GSON;
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
     * @param json json字符串
     * @param cls  类
     * @return 返回泛型
     */
    public static <T> List<T> fromListJson(final String json, final Class<T> cls) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(GSON.fromJson(elem, cls));
        }
        return list;
    }

    public static <T> List<T> fromListJsonType(final String json, final Class<T> clazz) {
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

    /**
     * 转成Object int to double 问题
     *
     * @return List<TreeMap < String, Object>>
     */
    public static List<TreeMap<String, Object>> jsonToMapList(String json) {
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

        List<TreeMap<String, Object>> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, new TypeToken<TreeMap<String, Object>>() {
            }.getType()));
        }
        return list;
    }

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

        JsonObject asJsonObject = new JsonParser().parse(json).getAsJsonObject();

        LinkedHashMap hashMap = gson.fromJson(asJsonObject, new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType());

        return hashMap;
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

    class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                //定义为int类型,如果后台返回""或者null,则返回0
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return null;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
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
