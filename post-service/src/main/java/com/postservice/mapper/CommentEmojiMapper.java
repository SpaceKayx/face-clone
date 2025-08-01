package com.postservice.mapper;

import com.postservice.dto.request.CommentEmojiRequest;
import com.postservice.entities.CommentEmoji;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentEmojiMapper {

    CommentEmoji mapToEntity(CommentEmojiRequest request);

}
