package com.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Objects;

@UtilityClass
@Log4j2
public class JSONUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Convert object to JSON string
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error converting object to JSON: {}", obj);
        }
        return null;
    }

    // Convert JSON string to object
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (Objects.isNull(json))
            return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            log.error("Error mapping JSON to object: {}", json);
        } catch (IOException e) {
            log.error("Error reading JSON: {}", json);
        } catch (Exception e) {
            log.error("Error converting JSON to object: {}", json);
        }

        return null;
    }

    // Convert JSON array string to Model
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (Objects.isNull(json))
            return null;
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            log.error("Error converting JSON to list: {}", json);
        }
        return null;
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> typeRef) {
        if (Objects.isNull(fromValue))
            return null;
        try {
            return objectMapper.convertValue(fromValue, typeRef);
        } catch (Exception e) {
            log.error("Error converting value to object: {}", fromValue);
        }
        return null;
    }
    
}
