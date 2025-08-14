//package com.postservice.async;
//
//import com.core.utils.DataCache;
//import com.core.utils.RedisUtil;
//import com.postservice.dto.response.PostLogsResponse;
//import com.postservice.entities.PostLogs;
//import com.postservice.entities.UserCache;
//import com.postservice.repositories.PostLogsRepository;
//import com.postservice.repositories.UserCacheRepository;
//import com.postservice.services.abs.PostLogsService;
//import com.postservice.services.abs.UserCacheService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@Slf4j
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
//public class SyncPostCache {
//
//    RedisUtil redisUtil;
//    PostLogsService postlogsService;
//    UserCacheService userCacheService;
//
//    @Async
//    public void updatePostInRedis(UserCache userCache) {
//        log.info("Start update post in redis");
//        List<PostLogs> posts = postlogsService.findAllByUserId(userCache.getId());
//
//        if (!posts.isEmpty()) {
//            for (PostLogs post : posts) {
//                String key = DataCache.getPostKeyInRedis(post.getId());
//                PostLogs dataCache = redisUtil.getDataFromRedis(key, PostLogs.class);
//
//                if (dataCache == null) continue;
//                this.buildAndSendDataToRedis(key, dataCache, userCache);
//            }
//        }
//        log.info("End update post in redis");
//    }
//
//    @Async
//    public void createPostInRedis(PostLogs postLogs) {
//        log.info("Start create post in redis");
//        UserCache user = userCacheService.getUserCacheById(postLogs.getUserId());
//        if (user == null) return;
//
//        this.buildAndSendDataToRedis(
//                DataCache.getPostKeyInRedis(postLogs.getId()),
//                postLogs, user);
//        log.info("End create post in redis");
//    }
//
//    private void buildAndSendDataToRedis(String key, PostLogs postLogs, UserCache user) {
//        redisUtil.setDataToRedis(
//                key,
//                PostLogsResponse.builder()
//                        .id(postLogs.getId())
//                        .title(postLogs.getTitle())
//                        .content(postLogs.getContent())
//                        .description(postLogs.getDescription())
//                        .privacy(postLogs.getPrivacy())
//                        .userId(user.getId())
//                        .username(user.getUsername())
//                        .firstName(user.getFirstName())
//                        .lastName(user.getLastName())
//                        .build()
//        );
//    }
//
//}
