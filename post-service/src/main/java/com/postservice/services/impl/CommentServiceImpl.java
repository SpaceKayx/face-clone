package com.postservice.services.impl;

import com.core.dto.response.PageableRequest;
import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentsRepository;
    CommentMapper commentMapper;
    RedisUtil redisUtil;

    public enum HandleRedis {DELETE, CREATE, UPDATE}

    @Override
    public Comment createComment(CommentRequest request) {
        Comment saved = commentsRepository.save(commentMapper.mapToEntity(request));
        handleDataWithRedis(saved, HandleRedis.CREATE);

        return saved;
    }

    @Override
    public void deleteComment(long id) {
        Comment existing = commentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentsRepository.delete(existing);
        handleDataWithRedis(existing, HandleRedis.DELETE);
    }

    @Override
    public Comment updateComment(long id, CommentRequest request) {
        Comment existing = commentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!existing.getContent().equals(request.getContent())) {
            existing.setContent(request.getContent());
            existing = commentsRepository.save(existing);
            handleDataWithRedis(existing, HandleRedis.UPDATE);
        }

        return existing;
    }

    /**
     * -------------------- GET COMMENTS --------------------
     */

    @Override
    public Map<UUID, List<CommentResponse>> getCommentsByPostIds(List<UUID> postIds, PageableRequest request) {
        if (postIds == null || postIds.isEmpty()) return Collections.emptyMap();

        Map<UUID, List<CommentResponse>> result = new HashMap<>();

        for (UUID postId : postIds) {
            List<Long> parentIds = redisUtil.getIdsFromZSet(
                    DataCache.getPostHasCommentParentKeyInRedis(postId),
                    request.getPage(),
                    request.getSize(),
                    Long::valueOf
            );

            List<CommentResponse> parentComments;

            if (!parentIds.isEmpty()) {
                List<String> keys = parentIds.stream()
                        .map(DataCache::getCommentKeyInRedis)
                        .toList();
                parentComments = redisUtil.multiGetFromRedis(keys, CommentResponse.class);
            } else {
                parentComments = commentsRepository.findParentCommentByPostIds(
                        postId,
                        request.getPageableRequest()
                );
                // cache lại
                parentComments.forEach(c -> handleDataWithRedis(commentMapper.mapToEntity(c), HandleRedis.CREATE));
            }

            result.put(postId, parentComments != null ? parentComments : Collections.emptyList());
        }

        return result;
    }

    @Override
    public List<CommentResponse> getCommentChildrenByParentId(long parentId, PageableRequest request) {
        if (parentId == 0) return Collections.emptyList();

        List<Long> childIds = redisUtil.getIdsFromZSet(
                DataCache.getPostHasCommentChildrenKeyInRedis(parentId),
                request.getPage(),
                request.getSize(),
                Long::valueOf
        );

        List<CommentResponse> children;

        if (!childIds.isEmpty()) {
            List<String> keys = childIds.stream()
                    .map(DataCache::getCommentKeyInRedis)
                    .toList();
            children = redisUtil.multiGetFromRedis(keys, CommentResponse.class);
        } else {
            children = commentsRepository.findChildrenByParentId(
                    parentId,
                    request.getPageableRequest()
            );
            children.forEach(c -> handleDataWithRedis(commentMapper.mapToEntity(c), HandleRedis.CREATE));
        }

        return children != null ? children : Collections.emptyList();
    }

    private void handleDataWithRedis(Comment comment, HandleRedis type) {
        boolean isParent = (comment.getParentId() == 0);
        switch (type) {
            case CREATE -> handleCreate(comment, isParent);
            case UPDATE -> handleUpdate(comment);
            case DELETE -> handleDelete(comment, isParent);
        }
    }

    private void handleCreate(Comment comment, boolean isParent) {
        String objectKey = DataCache.getCommentKeyInRedis(comment.getId());
        redisUtil.setDataToRedis(objectKey, comment);

        if (isParent) {
            redisUtil.addToZSet(
                    DataCache.getPostHasCommentParentKeyInRedis(comment.getPostId()),
                    comment.getId()
            );
        } else {
            redisUtil.addToZSet(
                    DataCache.getPostHasCommentChildrenKeyInRedis(comment.getParentId()),
                    comment.getId()
            );
        }
    }

    private void handleUpdate(Comment comment) {
        String objectKey = DataCache.getCommentKeyInRedis(comment.getId());
        redisUtil.setDataToRedis(objectKey, comment);
    }

    private void handleDelete(Comment comment, boolean isParent) {
        String objectKey = DataCache.getCommentKeyInRedis(comment.getId());

        if (isParent) {
            redisUtil.removeFromZSet(
                    DataCache.getPostHasCommentParentKeyInRedis(comment.getPostId()),
                    comment.getId()
            );
        } else {
            redisUtil.removeFromZSet(
                    DataCache.getPostHasCommentChildrenKeyInRedis(comment.getParentId()),
                    comment.getId()
            );
        }

        redisUtil.deleteDataFromRedis(objectKey);
    }

}
