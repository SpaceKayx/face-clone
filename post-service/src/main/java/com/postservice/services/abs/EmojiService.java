package com.postservice.services.abs;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.dto.response.EmojiResponse;
import com.postservice.entities.Emoji;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EmojiService {

    void createOrUpdateEmoji(EmojiRequest request, UUID userId);

    void deleteEmoji(UUID postId, UUID userId);

    Map<UUID, List<EmojiResponse>> findAllByPostIds(List<UUID> postIds);

}