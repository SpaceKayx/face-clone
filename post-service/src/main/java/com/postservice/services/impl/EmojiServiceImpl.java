package com.postservice.services.impl;

import com.core.constants.FConstants;
import com.core.dto.response.PageableRequest;
import com.core.kafka.message.BaseMessage;
import com.core.kafka.producer.BaseProducerHandler;
import com.core.utils.RedisUtil;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.dto.response.EmojiResponse;
import com.postservice.entities.Emoji;
import com.postservice.mapper.EmojiMapper;
import com.postservice.repositories.EmojiRepository;
import com.postservice.services.abs.EmojiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmojiServiceImpl implements EmojiService {

    EmojiRepository emojiRepository;
    EmojiMapper emojiMapper;
    BaseProducerHandler kafka;
    RedisUtil redisUtil;

    @Override
    public void deleteEmoji(UUID postId, UUID userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Emoji emoji = emojiRepository.findByUserIdAndPostId(userId, postId).orElseThrow(
                        () -> new ResourceNotFoundException("Emoji" + userId)
                );
                emojiRepository.deleteById(emoji.getId());
            }
        }).start();
    }

    @Override
    @Transactional
    public void createOrUpdateEmoji(EmojiRequest request, UUID userId) {
        Emoji emoji = emojiMapper.mapToEntity(request);
        emoji.setUserId(userId);

        kafka.send(BaseMessage.builder()
                .topic(FConstants.TOPIC_EMOJI_CACHE)
                .key(emoji.getUserId().toString())
                .value(emoji)
                .build());

        redisUtil.setDataToRedis(
                String.format(FConstants.TOPIC_EMOJI_CACHE_KEY, emoji.getUserId().toString()),
                emoji,
                Duration.ofDays(7));
    }

    @Override
    public Map<UUID, List<EmojiResponse>> findAllByPostIds(List<UUID> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<EmojiResponse> allEmojis = emojiRepository.findAllEmojiByPostIds(
                postIds,
                new PageableRequest(0, 50, "createTime", Sort.Direction.DESC).getPageableRequest());
        if (allEmojis.isEmpty()) {
            return Collections.emptyMap();
        }

        return allEmojis.stream()
                .collect(Collectors.groupingBy(EmojiResponse::getPostId));
    }

}
