package com.postservice.repositories;

import com.postservice.entities.PostLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLogsRepository extends JpaRepository<PostLogs, String> {
    List<PostLogs> findAllByUserId(String userId);
}
