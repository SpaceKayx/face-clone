package com.postservice.mapper;

import com.postservice.dto.request.EmojiRequest;
import com.postservice.entities.Emoji;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmojiMapper {

    Emoji mapToEntity(EmojiRequest request);

}
