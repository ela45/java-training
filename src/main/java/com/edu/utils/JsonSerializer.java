package com.edu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.edu.annotations.SkipJsonSerialization;

/**
 * Created by ycaceres on 9/30/17.
 */
public class JsonSerializer {
    public static <T> String serilaize(T source) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class objectClass = source.getClass();

        Field[] fields = objectClass.getDeclaredFields();
        List<String> values = new ArrayList<>(fields.length);

        for (int i = 0; i < fields.length; i++) {
            final Field f = fields[i];

            if(f.isAnnotationPresent(SkipJsonSerialization.class)) {
                continue;
            }

            Method fieldGetter = objectClass.getMethod(getGetterMethodName(f));
            Object result = fieldGetter.invoke(source);
            String resultAsString = result != null ? result.toString() : "null";
            values.add("'" + f.getName() + "' : '" + resultAsString + "'");
        }

        String jsonValue = Arrays.toString(values.toArray());
        return "{" + jsonValue.substring(1, jsonValue.length() - 1) + "}";
    }

    public static <T> T deserilaize(Class<T> target, String json) throws IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        T object = target.newInstance();

        String[] values = json.substring(1, json.length()).split(",\\s");

        for (int i = 0; i < values.length; i++) {
            String fieldName = values[i].substring(1, values[i].indexOf(":")-2);
            Object fieldValue = values[i].substring(values[i].indexOf(":") + 3, values[i].lastIndexOf("'"));
            final Field f = target.getDeclaredField(fieldName);

            if(f.isAnnotationPresent(SkipJsonSerialization.class)) {
                continue;
            }

            Method fieldSetter = target.getMethod(getSetterMethodName(f), f.getType());

            if (f.getType().getSimpleName().toLowerCase().contains("boolean")) {
                fieldValue = Boolean.parseBoolean(fieldValue.toString());
            } else if (f.getType().getSimpleName().toLowerCase().contains("int")) {
                fieldValue = Integer.parseInt(fieldValue.toString());
            }

            fieldSetter.invoke(object, fieldValue);
        }

        return object;
    }

    private static String getGetterMethodName(Field field) {
        String fieldName = field.getName();
        String camelFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String prefix = "get";
        if (field.getType().getSimpleName().toLowerCase().contains("boolean")) {
            prefix = "is";
        }
        return prefix + camelFieldName;
    }

    private static String getSetterMethodName(Field field) {
        String fieldName = field.getName();
        String camelFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return "set" + camelFieldName;
    }
}
