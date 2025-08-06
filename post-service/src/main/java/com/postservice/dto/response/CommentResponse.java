package com.postservice.dto.response;

import com.postservice.dto.request.CommentRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class CommentResponse extends CommentRequest {
    long id;
    List<CommentResponse> children;

    public CommentResponse(
            long id,
//            String postId,
            long parentId,
            String replyUserId,
            String content,
            UUID userId,
//            String username,
            String firstName,
            String lastName
    ) {
        this.id = id;
//        this.children = children;

//        this.setPostId(postId);
        this.setParentId(parentId);
        this.setReplyUserId(replyUserId);
        this.setContent(content);

        this.setUserId(userId);
//        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

}
