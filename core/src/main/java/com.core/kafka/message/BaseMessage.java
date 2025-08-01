package com.core.kafka.message;

import com.core.utils.JSONUtil;
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
    private Map<String, String> headers;



    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz, String message) {
        try {
            if (message == null || message.isEmpty()) {
                return clazz.getDeclaredConstructor().newInstance();
            }
            BaseMessage baseMessage =
                    JSONUtil.fromJson(message, BaseMessage.class);

            String valueJson = JSONUtil.toJson(Objects.requireNonNull(baseMessage).getValue());
            return JSONUtil.fromJson(valueJson, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse message or instantiate class: " + clazz.getName(), e);
        }
    }

}
