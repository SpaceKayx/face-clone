package com.postservice.dto.request;

import com.postservice.enums.MediaEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostMediaRequest {
    String mediaName;
    String mediaUrl;
    MediaEnum mediaType;
}
