package com.core.constants;

public interface FConstants {
    String USER_ID = "F-USER-ID";
    String BASE_USER = "Administrator";

    String GROUP_ID_DEFAULT = "1";

    String TOPIC_USER_FORGET_PW = "forget-password-send-mail";
    String TOPIC_USER_LOCKED = "user-locked-send-mail";
    String TOPIC_USER_CACHE = "user-cache";
    String TOPIC_USER_CACHE_KEY = "user-cache-%s";

    String TOPIC_COMMENT_DETAIL_CACHE = "comment-detail";
    String TOPIC_COMMENT_DETAIL_CACHE_KEY = "comment-detail-%s";

    String TOPIC_POST_OF_USER_CACHE = "post-of-user";
    String TOPIC_POST_OF_USER_CACHE_KEY = "post-of-user-%s";

    String TOPIC_POST_DETAIL_CACHE = "post-detail";
    String TOPIC_POST_DETAIL_CACHE_KEY = "post-detail-%s";

    String TOPIC_EMOJI_CACHE = "emoji-of-user";
    String TOPIC_EMOJI_CACHE_KEY = "emoji-of-user-%s";

}
