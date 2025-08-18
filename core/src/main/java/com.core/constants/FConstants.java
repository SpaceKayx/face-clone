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

    String TOPIC_COMMENT_OF_POST_CACHE = "comment-of-post";
    String TOPIC_COMMENT_PARENT_CACHE_KEY = "comment-parent-%s";
    String TOPIC_COMMENT_CHILDREN_CACHE_KEY = "comment-children-%s";

    String TOPIC_POST_OF_USER_CACHE = "post-of-user";
    String TOPIC_POST_OF_USER_CACHE_KEY = "post-of-user-%s";
    String TOPIC_POST_HAS_PARENT_COMMENT_CACHE_KEY = "post-has-parent-comment-%s";
    String TOPIC_POST_HAS_CHILDREN_COMMENT_CACHE_KEY = "post-has-children-comment-%s";

    String TOPIC_POST_DETAIL_CACHE = "post-detail";
    String TOPIC_POST_DETAIL_CACHE_KEY = "post-detail-%s";

    String TOPIC_EMOJI_CACHE = "emoji-of-user";
    String TOPIC_EMOJI_CACHE_KEY = "emoji-of-user-%s";

}
