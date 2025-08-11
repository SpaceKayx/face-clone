package com.postservice.kafka;

import com.core.constants.FConstants;
import com.core.kafka.BaseKafkaHandler;
import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
import com.postservice.async.SyncPostCache;
import com.postservice.entities.UserCache;
import com.postservice.repositories.UserCacheRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HandleUserCache extends BaseKafkaHandler<UserCache> {

    RedisUtil redisUtil;
    UserCacheRepository userCacheRepository;
    SyncPostCache syncPostCache;

    @KafkaListener(topics = FConstants.TOPIC_USER_CACHE,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        handleMessage(rawMessage, FConstants.TOPIC_USER_CACHE, UserCache.class);
    }

    @Override
    protected void processMessage(UserCache message) {
        userCacheRepository.save(message);
        redisUtil.setDataToRedis(
                DataCache.getUserKeyInRedis(message.getId()),
                message
        );

        syncPostCache.updatePostInRedis(message);
    }
}
