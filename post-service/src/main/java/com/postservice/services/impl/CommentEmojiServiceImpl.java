package com.postservice.services.impl;

import com.postservice.dto.request.CommentEmojiRequest;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.entities.CommentEmoji;
import com.postservice.entities.Emoji;
import com.postservice.mapper.CommentEmojiMapper;
import com.postservice.mapper.EmojiMapper;
import com.postservice.repositories.CommentEmojiRepository;
import com.postservice.repositories.EmojiRepository;
import com.postservice.services.abs.CommentEmojiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentEmojiServiceImpl implements CommentEmojiService {

    CommentEmojiRepository repository;
    CommentEmojiMapper mapper;

    @Override
    public CommentEmoji createEmoji(CommentEmojiRequest request, String userId) {
        CommentEmoji emoji = mapper.mapToEntity(request);
        emoji.setUserId(userId);

        return repository.save(emoji);
    }

    @Override
    public void deleteEmoji(String userId, long commentId) {
        CommentEmoji emoji = repository.findByUserIdAndCommentId(userId, commentId).orElseThrow(
                () -> new ResourceNotFoundException("Emoji", userId)
        );
        repository.deleteById(emoji.getId());
    }

    @Override
    public CommentEmoji updateEmoji(CommentEmojiRequest request, String userId) {
        return repository.findByUserIdAndCommentId(userId, request.getCommentId())
                .map(existingEmoji -> {
                    existingEmoji.setEmojiType(request.getEmojiType());
                    return repository.save(existingEmoji);
                })
                .orElseGet(() -> this.createEmoji(request, userId));
    }

}
