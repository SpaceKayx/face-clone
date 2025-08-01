package com.postservice.services.impl;

import com.postservice.dto.request.PostLogsRequest;
import com.postservice.entities.PostLogs;
import com.postservice.exception.BaseException;
import com.postservice.exception.ErrorCode;
import com.postservice.mapper.PostLogsMapper;
import com.postservice.repositories.PostLogsRepository;
import com.postservice.services.abs.PostLogsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLogsServiceImpl implements PostLogsService {

    PostLogsRepository postLogsRepository;
    PostLogsMapper postLogsMapper;

    @Override
    public PostLogs createPost(PostLogsRequest request, String userId) {
        PostLogs postLogs = postLogsMapper.mapToEntity(request);
        postLogs.setUserId(userId);
        return postLogsRepository.save(postLogs);
    }

    @Override
    public PostLogs updatePost(String id, PostLogsRequest request) {
        PostLogs existingPost = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));

        postLogsMapper.updatePostLogsFromRequest(request, existingPost);
        existingPost.setId(existingPost.getId());

        return postLogsRepository.save(existingPost);
    }


    @Override
    public void deletePost(String id) {
        PostLogs postLogs = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
        postLogs.setDeleted(true);

        postLogsRepository.save(postLogs);
    }

    @Override
    public PostLogs getPostById(String id) {
        return postLogsRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
    }

    @Override
    public List<PostLogs> getAllPosts() {
        return postLogsRepository.findAll();
    }

    @Override
    public List<PostLogs> findAllByUserId(String userId) {
        return postLogsRepository.findAllByUserId(userId);
    }
}
