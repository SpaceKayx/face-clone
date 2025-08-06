package com.postservice.kafka;

import com.core.constants.FConstants;
import com.core.kafka.message.BaseMessage;
import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
import com.postservice.async.SyncCommentCache;
import com.postservice.async.SyncPostCache;
import com.postservice.entities.UserCache;
import com.postservice.repositories.UserCacheRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HandleUserCache {

    RedisUtil redisUtil;
    UserCacheRepository userCacheRepository;
    SyncPostCache syncPostCache;
//    SyncCommentCache syncCommentCache;

//    HandleUserCache syncUserCache; // inject chính nó dể call proxy

    @KafkaListener(topics = FConstants.TOPIC_USER_CACHE,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        log.info("Start save cache user");
        if (rawMessage == null) return;
        try {
            UserCache message = new BaseMessage().getValue(UserCache.class, rawMessage);
//            if (message == null) {
//                log.error("Cannot parse to user-cache");
//                return;
//            }
            userCacheRepository.save(message);
            redisUtil.setDataToRedis(
                    DataCache.getUserKeyInRedis(message.getId()),
                    message
            );

//            syncCommentCache.updateCommentInRedis(message);
            syncPostCache.updatePostInRedis(message);

            log.info("Added/Updated in database and redis user cache with id {}", message.getId());
            log.info("End save cache user");
        } catch (Exception e) {
            log.error("Error save cache user: {}", e.getMessage(), e);
        }
    }

}
