package com.postservice.repositories;

import com.postservice.dto.response.EmojiResponse;
import com.postservice.entities.Emoji;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    Optional<Emoji> findByUserIdAndPostId(UUID userId, UUID postId);

    void deleteByUserIdAndPostId(UUID userId, UUID postId);

    @Query("""
            SELECT new com.postservice.dto.response.EmojiResponse(e.id, e.emojiType, u.id, e.postId, u.firstName, u.lastName)
            FROM Emoji e
            INNER JOIN UserCache u ON e.userId = u.id
            WHERE e.postId = :postId
            """)
    Optional<List<EmojiResponse>> findAllEmojiPostId(UUID postId);

    @Query("""
            SELECT new com.postservice.dto.response.EmojiResponse(e.id, e.emojiType, u.id, e.postId, u.firstName, u.lastName)
            FROM Emoji e
            INNER JOIN UserCache u ON e.userId = u.id
            WHERE e.postId IN :postIds
            """)
    List<EmojiResponse> findAllEmojiByPostIds(List<UUID> postIds, Pageable pageable);

}
