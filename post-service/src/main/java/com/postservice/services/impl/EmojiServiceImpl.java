package com.postservice.services.impl;

import com.core.constants.FConstants;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

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
        Emoji emoji = emojiRepository.findByUserIdAndPostId(userId, postId).orElseThrow(
                () -> new ResourceNotFoundException("Emoji" + userId)
        );
        emojiRepository.deleteById(emoji.getId());
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
    public List<EmojiResponse> findAllByPostId(UUID postId) {
        return emojiRepository.findAllEmojiPostId(postId).orElse(List.of());
    }

}
