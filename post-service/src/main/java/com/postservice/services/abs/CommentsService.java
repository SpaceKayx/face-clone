package com.postservice.services.abs;

import com.postservice.dto.request.CommentRequest;
import com.postservice.entities.Comment;

public interface CommentsService {

    Comment createComment(CommentRequest request);

    void deleteComment(long id);

    Comment updateComment(long id, CommentRequest request);

}