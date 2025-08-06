package com.postservice.services.impl;

import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
import com.postservice.async.SyncPostCache;
import com.postservice.dto.request.PostLogsRequest;
import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import com.postservice.exception.BaseException;
import com.postservice.exception.ErrorCode;
import com.postservice.mapper.PostLogsMapper;
import com.postservice.repositories.PostLogsRepository;
import com.postservice.services.abs.CommentService;
import com.postservice.services.abs.EmojiService;
import com.postservice.services.abs.PostLogsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLogsServiceImpl implements PostLogsService {

    PostLogsRepository postLogsRepository;
    PostLogsMapper postLogsMapper;
    RedisUtil redisUtil;
//    UserCacheService userCacheService;
    CommentService commentsService;
    EmojiService emojiService;
    SyncPostCache syncPostCache;

    @Override
    public PostLogs createPost(PostLogsRequest request, UUID userId) {
        PostLogs postLogs = postLogsMapper.mapToEntity(request);
        postLogs.setUserId(userId);

        PostLogs response = postLogsRepository.save(postLogs);
        syncPostCache.createPostInRedis(response);

        return response;
    }

    @Override
    public PostLogs updatePost(UUID id, PostLogsRequest request) {
        PostLogs existingPost = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));

        postLogsMapper.updatePostLogsFromRequest(request, existingPost);
        existingPost.setId(existingPost.getId());

        syncPostCache.createPostInRedis(existingPost);

        return postLogsRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        PostLogs postLogs = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
        postLogs.setDeleted(true);

        redisUtil.deleteDataFromRedis(DataCache.getPostKeyInRedis(id));
        postLogsRepository.save(postLogs);
    }

    @Override
    public PostLogsResponse getPostById(UUID id) {
        PostLogsResponse response = redisUtil.getDataFromRedis(
                DataCache.getPostKeyInRedis(id),
                PostLogsResponse.class
        );

        if (response == null) {
            response = postLogsRepository.findByPostId(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
        }
        response.setComments(commentsService.getCommentByPostId(id));
        response.setEmojis(emojiService.findAllByPostId(id));

        return response;
    }

    @Override
    public List<PostLogs> getAllPosts() {
        return postLogsRepository.findAll();
    }

    @Override
    public List<PostLogs> findAllByUserId(UUID userId) {
        return postLogsRepository.findAllByUserId(userId);
    }
}
