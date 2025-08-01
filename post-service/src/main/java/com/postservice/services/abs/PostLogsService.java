package com.postservice.services.abs;

import com.postservice.dto.request.PostLogsRequest;
import com.postservice.entities.PostLogs;
import java.util.List;

public interface PostLogsService {

    PostLogs createPost(PostLogsRequest request, String userId);

    PostLogs updatePost(String id, PostLogsRequest request);

    void deletePost(String id);

    PostLogs getPostById(String id);

    List<PostLogs> getAllPosts();

    List<PostLogs> findAllByUserId(String userId);

}