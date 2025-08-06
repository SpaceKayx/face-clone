package com.postservice.services.abs;
import com.postservice.dto.request.CommentEmojiRequest;
import com.postservice.entities.CommentEmoji;

import java.util.UUID;

public interface CommentEmojiService {

    CommentEmoji createEmoji(CommentEmojiRequest request, UUID userId);

    void deleteEmoji(long commentId);

    CommentEmoji updateEmoji(CommentEmojiRequest request, UUID userId);

}