package com.postservice.mapper;

import com.postservice.dto.request.CommentRequest;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.entities.Comment;
import com.postservice.entities.Emoji;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapToEntity(CommentRequest request);

}
