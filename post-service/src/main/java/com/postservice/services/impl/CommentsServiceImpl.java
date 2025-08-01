package com.postservice.services.impl;

import com.postservice.dto.request.CommentRequest;
import com.postservice.entities.Comment;
import com.postservice.mapper.CommentMapper;
import com.postservice.repositories.CommentsRepository;
import com.postservice.services.abs.CommentsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentsServiceImpl implements CommentsService {

    CommentsRepository commentsRepository;
    CommentMapper commentMapper;

    @Override
    public Comment createComment(CommentRequest request) {
        return commentsRepository.save(commentMapper.mapToEntity(request));
    }

    @Override
    public void deleteComment(long id) {
        commentsRepository.deleteById(id);
    }

    @Override
    public Comment updateComment(long id, CommentRequest request) {
        Comment existingComment = commentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        request.setContent(request.getContent());

        return commentsRepository.save(existingComment);
    }
}
