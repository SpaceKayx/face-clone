package com.postservice.services.impl;

import com.postservice.dto.request.EmojiRequest;
import com.postservice.entities.Emoji;
import com.postservice.mapper.EmojiMapper;
import com.postservice.repositories.EmojiRepository;
import com.postservice.services.abs.EmojiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmojiServiceImpl implements EmojiService {

    EmojiRepository emojiRepository;
    EmojiMapper emojiMapper;

    @Override
    public Emoji createEmoji(EmojiRequest request, String userId) {
        Emoji emoji = emojiMapper.mapToEntity(request);
        emoji.setUserId(userId);

        return emojiRepository.save(emoji);
    }

    @Override
    public void deleteEmoji(UUID postId, String userId) {
        Emoji emoji = emojiRepository.findByUserIdAndPostId(userId, postId).orElseThrow(
                () -> new ResourceNotFoundException("Emoji", userId)
        );
        emojiRepository.deleteById(emoji.getId());
    }

    @Override
    public Emoji updateEmoji(EmojiRequest request, String userId) {
        return emojiRepository.findByUserIdAndPostId(userId, request.getPostId())
                .map(existingEmoji -> {
                    existingEmoji.setEmojiType(request.getEmojiType());
                    return emojiRepository.save(existingEmoji);
                })
                .orElseGet(() -> this.createEmoji(request, userId));
    }

}
