package com.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> T getDataFromRedis(String key, Class<T> clazz) {
        Object json = redisTemplate.opsForValue().get(key);
        if (json instanceof String str) {
            return JSONUtil.fromJson(str, clazz);
        }
        return null;
    }

    // Set dữ liệu vào Redis có thời hạn (Duration.ofMinutes(10))
    @Async
    public void setDataToRedis(String key, Object value, Duration ttl) {
        String json = JSONUtil.toJson(value);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, ttl);
        }
    }

    @Async
    public void setDataToRedis(String key, Object value) {
        String json = JSONUtil.toJson(value);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json);
        }
    }

    public void deleteDataFromRedis(String key) {
        redisTemplate.delete(key);
    }

}
