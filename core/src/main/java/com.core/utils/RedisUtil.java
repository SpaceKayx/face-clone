package com.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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

    @Async
    public void setDataToRedisWithRealTime(String key, Object value) {
        if (Objects.isNull(value)) return;

        redisTemplate.opsForZSet().add(
                key,
                Objects.requireNonNull(JSONUtil.toJson(value)),
                System.currentTimeMillis());
    }

    public <T> List<T> getDataFromRedis(String key, long start, long end, Class<T> clazz) {
        Set<Object> raw = redisTemplate.opsForZSet().reverseRange(key, start, end); // reverseRange => mới nhất trước

        if (raw == null) return List.of();

        return raw.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(json -> JSONUtil.fromJson(json, clazz))
                .toList();
    }

    public <T> List<T> getKeysFromZSet(String key, long start, long end, Function<String, T> mapper) {
        Set<Object> raw = redisTemplate.opsForZSet().reverseRange(key, start, end);
        if (raw == null) return List.of();

        return raw.stream()
                .filter(Objects::nonNull)
                .map(obj -> mapper.apply(obj.toString())) // co the dung ví du:  UUID::fromString
                .toList();
    }

    public <T> List<T> multiGetFromRedis(List<String> keys, Class<T> clazz) {
        if (keys == null || keys.isEmpty()) return List.of();

        List<Object> rawList = redisTemplate.opsForValue().multiGet(keys);
        if (rawList == null) return List.of();

        return rawList.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(json -> JSONUtil.fromJson(json, clazz))
                .toList();
    }

    public List<Object> multiGetFromRedis(List<String> keys) {
        if (keys == null || keys.isEmpty()) return List.of();

        List<Object> rawList = redisTemplate.opsForValue().multiGet(keys);

        return rawList == null ? List.of() : rawList;
    }

    @Async
    public void deleteDataFromRedis(String key) {
        redisTemplate.delete(key);
    }

    @Async
    public void removeFromZSet(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, JSONUtil.toJson(value));
    }

}
