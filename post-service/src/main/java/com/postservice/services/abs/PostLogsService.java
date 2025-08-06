package com.postservice.services.abs;

import com.postservice.dto.request.PostLogsRequest;
import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import java.util.List;
import java.util.UUID;

public interface PostLogsService {

    PostLogs createPost(PostLogsRequest request, UUID userId);

    PostLogs updatePost(UUID id, PostLogsRequest request);

    void deletePost(UUID id);

    PostLogsResponse getPostById(UUID id);

    List<PostLogs> getAllPosts();

    List<PostLogs> findAllByUserId(UUID userId);

}