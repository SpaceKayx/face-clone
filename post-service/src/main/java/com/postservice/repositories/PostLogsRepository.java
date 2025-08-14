package com.postservice.repositories;

import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostLogsRepository extends JpaRepository<PostLogs, UUID> {

    @Query(value = """
            SELECT new com.postservice.dto.response.PostLogsResponse(p.id, p.title, p.content, p.description, p.privacy, u.id, u.firstName, u.lastName)
            FROM PostLogs p
            INNER JOIN UserCache u
                ON u.id = p.userId
            WHERE p.deleted = false
            AND p.id = :postId
            """)
    Optional<PostLogsResponse> findByPostId(UUID postId);

    @Query(value = """
            SELECT new com.postservice.dto.response.PostLogsResponse(p.id, p.title, p.content, p.description, p.privacy, u.id, u.firstName, u.lastName)
            FROM PostLogs p
            INNER JOIN UserCache u
            ON u.id = p.userId
            WHERE p.deleted = false
            AND u.id = :userId
            """)
    List<PostLogsResponse> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByUserId(UUID userId);
}
