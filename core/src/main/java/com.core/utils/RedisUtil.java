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

    /**
     * -------------------- Value --------------------
     */

    public <T> T getDataFromRedis(String key, Class<T> clazz) {
        Object json = redisTemplate.opsForValue().get(key);
        if (json instanceof String str) {
            return JSONUtil.fromJson(str, clazz);
        }
        return null;
    }

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

    /**
     * -------------------- ZSet --------------------
     */

    // add id vào ZSet, score = timestamp
    @Async
    public void addToZSet(String key, Object id) {
        if (Objects.isNull(id)) return;
        redisTemplate.opsForZSet().add(key, id.toString(), System.currentTimeMillis());
    }

    // phân trang bằng PageableRequest
    public <T> List<T> getIdsFromZSet(String key, int page, int size, Function<String, T> mapper) {
        long start = (long) page * size;
        long end = start + size - 1;

        Set<Object> raw = redisTemplate.opsForZSet().reverseRange(key, start, end);
        if (raw == null) return List.of();

        return raw.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(mapper) // ví dụ Long::valueOf hoặc UUID::fromString
                .toList();
    }

    @Async
    public void removeFromZSet(String key, Object id) {
        if (id != null) {
            redisTemplate.opsForZSet().remove(key, id.toString());
        }
    }

    /**
     * -------------------- MultiGet --------------------
     */

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

    /**
     * -------------------- Delete --------------------
     */

    @Async
    public void deleteDataFromRedis(String key) {
        redisTemplate.delete(key);
    }

}
