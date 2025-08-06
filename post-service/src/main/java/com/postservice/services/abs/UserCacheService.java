package com.postservice.services.abs;

import com.postservice.entities.UserCache;

import java.util.UUID;

public interface UserCacheService {
    UserCache getUserCacheById(UUID id);
}
