package com.postservice.services.abs;

import com.postservice.dto.request.CommentRequest;
import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CommentService {

    Comment createComment(CommentRequest request);

    void deleteComment(long id);

    Comment updateComment(long id, CommentRequest request);

    Map<UUID, List<CommentResponse>> getCommentsByPostIds(List<UUID> postIds);

}