package com.postservice.dto.response;

import com.core.utils.HttpServletRequestUtil;
import com.postservice.enums.EmojiEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmojiResponse extends UserResponse implements Serializable {
    long id;
    //    String postId;
    EmojiEnum emojiType;
    boolean isYourEmoji = false;

    public EmojiResponse(long id, EmojiEnum emojiType, UUID userId, String firstName, String lastName) {
        this.id = id;
        this.emojiType = emojiType;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isYourEmoji = userId.equals(HttpServletRequestUtil.getCurrentUserId());
    }
}
