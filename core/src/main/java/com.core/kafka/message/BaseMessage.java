package com.core.kafka.message;

import com.core.utils.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BaseMessage {
    private String topic;
    private String key;
    private Object value;
    private Integer partition;
    private Map<String, String> headers;


    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz, String message) {
        try {
            if (message == null || message.isEmpty()) {
                return clazz.getDeclaredConstructor().newInstance();
            }

            Map<String, Object> map = JSONUtil.fromJson(message, new TypeReference<Map<String, Object>>() {
            });
            if (map == null || !map.containsKey("value")) return null;

            String valueJson = JSONUtil.toJson(map.get("value"));

            return JSONUtil.fromJson(valueJson, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse message or instantiate class: " + clazz.getName(), e);
        }
    }


}
