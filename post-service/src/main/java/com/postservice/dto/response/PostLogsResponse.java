package com.postservice.dto.response;

import com.postservice.enums.PrivacyEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLogsResponse {
    UUID id;
    String title;
    String content;
    String description;
    PrivacyEnum privacy;
    List<CommentResponse> comments;
}
