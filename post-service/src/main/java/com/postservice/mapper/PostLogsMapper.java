package com.postservice.mapper;

import com.postservice.dto.request.PostLogsRequest;
import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostLogsMapper {

    PostLogs mapToEntity(PostLogsRequest request);
    PostLogsResponse mapToResponse(PostLogs request);
    void updatePostLogsFromRequest(PostLogsRequest request, @MappingTarget PostLogs postLogs);

}
