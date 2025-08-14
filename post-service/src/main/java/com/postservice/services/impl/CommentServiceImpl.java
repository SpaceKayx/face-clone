package com.postservice.services.impl;

import com.core.dto.response.PageableRequest;
import com.postservice.dto.request.CommentRequest;
import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;
import com.postservice.mapper.CommentMapper;
import com.postservice.repositories.CommentRepository;
import com.postservice.services.abs.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        existingComment.setContent(request.getContent());

        return commentsRepository.save(existingComment);
    }

    @Override
    public Map<UUID, List<CommentResponse>> getCommentsByPostIds(List<UUID> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<CommentResponse> allComments = commentsRepository.findAllCommentByPostIds(
                postIds,
                new PageableRequest(0, 50, "createTime", Sort.Direction.DESC).getPageableRequest());
        if (allComments.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<UUID, List<CommentResponse>> grouped = new HashMap<>();
        for (CommentResponse allComment : allComments) {
            grouped.computeIfAbsent(allComment.getPostId(), k -> new ArrayList<>()).add(allComment);
        }

        grouped.replaceAll((postId, comments) -> buildCommentTree(comments));

        return grouped;
    }

    private List<CommentResponse> buildCommentTree(List<CommentResponse> comments) {
        Map<Long, CommentResponse> commentById = comments.stream()
                .collect(Collectors.toMap(CommentResponse::getId, c -> c));

        List<CommentResponse> roots = new ArrayList<>();
        for (CommentResponse comment : comments) {
            if (comment.getParentId() == 0) {
                roots.add(comment);
            } else {
                CommentResponse parent = commentById.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }
        return roots;
    }


}
