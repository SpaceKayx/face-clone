package com.postservice.services.abs;

import com.core.dto.response.PageableRequest;
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

    Map<UUID, List<CommentResponse>> getCommentsByPostIds(List<UUID> postIds, PageableRequest request);

    List<CommentResponse> getCommentChildrenByParentId(long parentId, PageableRequest request);

}