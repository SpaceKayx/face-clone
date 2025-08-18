package com.postservice.repositories;

import com.postservice.dto.response.CommentResponse;
import com.postservice.entities.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(UUID userId);

    @Query("""
            SELECT new com.postservice.dto.response.CommentResponse(c.id, c.parentId, c.replyUserId, c.content,u.id, u.firstName, u.lastName)
            FROM Comment c
            INNER JOIN UserCache u
                ON c.userId = u.id
            WHERE c.postId = :postId
            """)
    List<CommentResponse> findAllCommentByPostId(UUID postId);

    @Query("""
            SELECT new com.postservice.dto.response.CommentResponse(c.id, c.parentId, c.replyUserId, c.content,u.id, u.firstName, u.lastName)
            FROM Comment c
            INNER JOIN UserCache u
                ON c.userId = u.id
            WHERE c.postId = :postIds
            AND c.parentId = 0
            """)
    List<CommentResponse> findParentCommentByPostIds(UUID postIds, Pageable pageable);

    @Query("""
            SELECT new com.postservice.dto.response.CommentResponse(c.id, c.parentId, c.replyUserId, c.content,u.id, u.firstName, u.lastName)
            FROM Comment c
            INNER JOIN UserCache u
                ON c.userId = u.id
            WHERE c.parentId <> 0
            AND c.parentId = :parentId
            """)
    List<CommentResponse> findChildrenByParentId(long parentId, Pageable pageable);

}
