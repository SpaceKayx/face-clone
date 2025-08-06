package com.core.utils;

import com.core.constants.FConstants;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataCache {

    public static String getCommentKeyInRedis(long key) {
        return String.format(FConstants.TOPIC_COMMENT_DETAIL_CACHE_KEY, key);
    }

    public static String getPostKeyInRedis(UUID key) {
        return String.format(FConstants.TOPIC_POST_DETAIL_CACHE_KEY, key);
    }

    public static String getUserKeyInRedis(UUID key) {
        return String.format(FConstants.TOPIC_USER_CACHE_KEY, key);
    }

}
