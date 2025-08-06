package com.postservice.dto.response;

import com.postservice.enums.PrivacyEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLogsResponse extends UserResponse implements Serializable {
    UUID id;
    String title;
    String content;
    String description;
    PrivacyEnum privacy;
    List<CommentResponse> comments; // redis kh save này
    List<EmojiResponse> emojis; // redis kh save này

    public PostLogsResponse(UUID id, String title, String content, String description,
                            PrivacyEnum privacy, UUID userId, String firstName, String lastName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.privacy = privacy;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
