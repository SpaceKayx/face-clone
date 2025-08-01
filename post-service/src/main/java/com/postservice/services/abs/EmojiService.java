package com.postservice.services.abs;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.entities.Emoji;

import java.util.UUID;

public interface EmojiService {

    Emoji createEmoji(EmojiRequest request, String userId);

    void deleteEmoji(UUID postId, String userId);

    Emoji updateEmoji(EmojiRequest request, String userId);

}