package com.postservice.async;

import com.core.constants.FConstants;
import com.core.utils.DataCache;
import com.core.utils.JSONUtil;
import com.core.utils.RedisUtil;
import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;
import com.postservice.entities.UserCache;
import com.postservice.repositories.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SyncCommentCache {

    RedisUtil redisUtil;
    CommentRepository commentRepository;

    @Async
    public void updateCommentInRedis(UserCache userCache) {
        log.info("Start update comment in redis");
        List<Comment> comments = commentRepository.findAllByUserId(userCache.getId());

        if (!comments.isEmpty()) {

            for (Comment comment : comments) {
                String key = DataCache.getCommentKeyInRedis(comment.getId());

                CommentResponse dataCache = redisUtil.getDataFromRedis(key, CommentResponse.class);
                if (dataCache == null) continue;
                redisUtil.setDataToRedis(
                        key,
                        CommentResponse.builder()
                                .id(dataCache.getId())
                                .postId(dataCache.getPostId())
                                .parentId(dataCache.getParentId())
                                .replyUserId(dataCache.getReplyUserId())
                                .content(dataCache.getContent())
                                .children(dataCache.getChildren())
                                .userId(userCache.getId())
                                .username(userCache.getUsername())
                                .firstName(userCache.getFirstName())
                                .lastName(userCache.getLastName())
                                .build()
                );
            }
        }
        log.info("End update comment in redis");
    }

}
