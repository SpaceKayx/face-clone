package com.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class StringUtil {

    public static String generatedStringFormat(String concatWith, Object... data) {
        return String.join(concatWith,
                java.util.Arrays.stream(data)
                        .map(String::valueOf)
                        .toArray(String[]::new));
    }

    @SuppressWarnings("unchecked")
    public static <C extends Collection<T>, T> C convertCollectionElements(Object obj, Class<T> elementType) {
        if (obj == null) return null;

        Collection<?> sourceCollection;

        // Nếu là Array → List
        if (obj.getClass().isArray()) {
            sourceCollection = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection<?>) {
            sourceCollection = (Collection<?>) obj;
        } else {
            throw new IllegalArgumentException("Object is not a collection or array: " + obj.getClass());
        }

        C targetCollection;

        try {
            // Tạo instance mới cùng kiểu (List, Set...)
            targetCollection = (C) obj.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Nếu không tạo được thì fallback về ArrayList
            targetCollection = (C) new ArrayList<T>();
        }

        for (Object element : sourceCollection) {
            targetCollection.add(convertTo(element, elementType));
        }

        return targetCollection;
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertTo(Object obj, Class<T> targetType) {
        if (obj == null) return null;

        String strValue = obj.toString();
        if (targetType == String.class) return (T) strValue;
        if (targetType == Integer.class || targetType == int.class) return (T) Integer.valueOf(strValue);
        if (targetType == Long.class || targetType == long.class) return (T) Long.valueOf(strValue);
        if (targetType == Double.class || targetType == double.class) return (T) Double.valueOf(strValue);
        if (targetType == Float.class || targetType == float.class) return (T) Float.valueOf(strValue);
        if (targetType == Boolean.class || targetType == boolean.class) return (T) Boolean.valueOf(strValue);
        if (targetType == UUID.class) return (T) UUID.fromString(strValue);

        return JSONUtil.fromJson(strValue, targetType);
    }


}
