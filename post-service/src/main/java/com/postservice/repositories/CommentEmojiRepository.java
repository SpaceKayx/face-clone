package com.postservice.repositories;

import com.postservice.entities.CommentEmoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {
    Optional<CommentEmoji> findByUserIdAndCommentId(UUID userId, long commentId);
//    void deleteByUserIdAndPostId(String userId, UUID postId);
}
