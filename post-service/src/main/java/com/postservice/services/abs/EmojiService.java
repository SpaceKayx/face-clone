package com.postservice.services.abs;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.dto.response.EmojiResponse;
import com.postservice.entities.Emoji;

import java.util.List;
import java.util.UUID;

public interface EmojiService {

    Emoji createOrUpdateEmoji(EmojiRequest request, UUID userId);

    void deleteEmoji(UUID postId, UUID userId);

    Emoji createEmoji(EmojiRequest request, UUID userId);

    List<EmojiResponse> findAllByPostId(UUID postId);

}