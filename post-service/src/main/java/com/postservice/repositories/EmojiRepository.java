package com.postservice.repositories;

import com.postservice.entities.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    Optional<Emoji> findByUserIdAndPostId(String userId, UUID postId);
    void deleteByUserIdAndPostId(String userId, UUID postId);
}
