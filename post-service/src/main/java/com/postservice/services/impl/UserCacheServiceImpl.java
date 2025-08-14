package com.postservice.services.impl;

import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
import com.postservice.entities.UserCache;
import com.postservice.repositories.UserCacheRepository;
import com.postservice.services.abs.UserCacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCacheServiceImpl implements UserCacheService {

    UserCacheRepository userCacheRepository;
    RedisUtil redisUtil;

    @Override
    public UserCache getUserCacheById(UUID id) {
        if (id == null) return null;

        UserCache user = redisUtil.getDataFromRedis(
                DataCache.getUserKeyInRedis(id),
                UserCache.class);

        if (user == null) {
            user = userCacheRepository.findById(id).orElse(null);

            if (user != null) {
                redisUtil.setDataToRedis(
                        DataCache.getUserKeyInRedis(id),
                        user
                );
            }
        }

        return user;
    }

}
