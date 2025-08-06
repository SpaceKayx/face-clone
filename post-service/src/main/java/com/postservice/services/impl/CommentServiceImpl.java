package com.postservice.services.impl;

import com.postservice.dto.request.CommentRequest;
import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;
import com.postservice.mapper.CommentMapper;
import com.postservice.repositories.CommentRepository;
import com.postservice.services.abs.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentsRepository;
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

    @Override
    public List<CommentResponse> getCommentByPostId(UUID postId) {
        List<CommentResponse> comments = commentsRepository.findAllCommentByPostId(postId);
        List<CommentResponse> response = new ArrayList<>();

        for (CommentResponse comment : comments) {
            if (comment.getParentId() == 0) {
                comment.setChildren(buildChildren(comment, comments));
                response.add(comment);
            }
        }
        return response;
    }

    private List<CommentResponse> buildChildren(CommentResponse currentElement, List<CommentResponse> lists) {
        List<CommentResponse> response = new ArrayList<>();

        for (CommentResponse comment : lists) {
            if (comment.getParentId() == currentElement.getId()) {
                comment.setChildren(buildChildren(comment, lists));
                response.add(comment);
            }
        }

        return response;
    }

}
