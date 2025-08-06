package com.postservice.mapper;

import com.postservice.dto.request.CommentRequest;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;
import com.postservice.entities.Emoji;
import com.postservice.entities.UserCache;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapToEntity(CommentRequest request);
    CommentResponse mapToResponse(Comment request);

    default CommentResponse mapRequestToResponse(CommentRequest request, Comment entity) {
        return CommentResponse.builder()
                .id(entity.getId())
                .postId(request.getPostId())
                .parentId(request.getParentId())
                .replyUserId(request.getReplyUserId())
                .content(request.getContent())
                .userId(request.getUserId())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    default CommentResponse mapUserToResponse(UserCache user, Comment entity) {
        return CommentResponse.builder()
                .id(entity.getId())
//                .postId(entity.getPostId())
                .parentId(entity.getParentId())
                .replyUserId(entity.getReplyUserId())
                .content(entity.getContent())
                .userId(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
