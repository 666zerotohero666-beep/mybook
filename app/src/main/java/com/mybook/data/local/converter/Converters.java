package com.mybook.data.local.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Room类型转换器
 * 用于处理Room不支持的类型转换
 */
public class Converters {
    private static final Gson gson = new Gson();

    /**
     * 将List<String>转换为JSON字符串
     * @param strings List<String>对象
     * @return JSON字符串
     */
    @TypeConverter
    public static String fromStringList(List<String> strings) {
        if (strings == null) {
            return null;
        }
        return gson.toJson(strings);
    }

    /**
     * 将JSON字符串转换为List<String>
     * @param json JSON字符串
     * @return List<String>对象
     */
    @TypeConverter
    public static List<String> toStringList(String json) {
        if (json == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}