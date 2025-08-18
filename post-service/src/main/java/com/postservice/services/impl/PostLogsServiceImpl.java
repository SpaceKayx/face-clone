package com.postservice.services.impl;

import com.core.dto.response.PageableRequest;
import com.core.utils.DataCache;
import com.core.utils.RedisUtil;
import com.core.utils.StringUtil;
import com.postservice.dto.request.PostLogsRequest;
import com.postservice.dto.response.CommentResponse;
import com.postservice.dto.response.EmojiResponse;
import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import com.postservice.entities.UserCache;
import com.postservice.exception.BaseException;
import com.postservice.exception.ErrorCode;
import com.postservice.mapper.PostLogsMapper;
import com.postservice.repositories.PostLogsRepository;
import com.postservice.services.abs.CommentService;
import com.postservice.services.abs.EmojiService;
import com.postservice.services.abs.PostLogsService;
import com.postservice.services.abs.UserCacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLogsServiceImpl implements PostLogsService {

    PostLogsRepository postLogsRepository;
    PostLogsMapper postLogsMapper;
    RedisUtil redisUtil;
    CommentService commentsService;
    EmojiService emojiService;
    UserCacheService userCacheService;

    @Override
    public PostLogs createPost(PostLogsRequest request, UUID userId) {
        PostLogs entity = postLogsMapper.mapToEntity(request);
        entity.setUserId(userId);

        entity = postLogsRepository.save(entity);

        redisUtil.setDataToRedis(getKeyInRedis(entity.getId()), entity);
        redisUtil.addToZSet(DataCache.getPostOfUserKeyInRedis(userId), entity.getId());

        return entity;
    }

    @Override
    public PostLogs updatePost(UUID id, PostLogsRequest request) {
        PostLogs existingPost = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));

        postLogsMapper.updatePostLogsFromRequest(request, existingPost);

        redisUtil.setDataToRedis(getKeyInRedis(existingPost.getId()), existingPost);
        return postLogsRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        PostLogs postLogs = postLogsRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
        postLogs.setDeleted(true);

        redisUtil.deleteDataFromRedis(getKeyInRedis(id));
        postLogsRepository.save(postLogs);
    }

    @Override
    public PostLogsResponse getPostById(UUID id) {
        PostLogsResponse response = redisUtil.getDataFromRedis(getKeyInRedis(id), PostLogsResponse.class);

        if (response == null) {
            response = postLogsRepository.findByPostId(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.POST_NOTFOUND));
        }

        UserCache user = userCacheService.getUserCacheById(response.getUserId());
        if (user != null) {
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setUsername(user.getUsername());
        }

        response.setComments(commentsService.getCommentsByPostIds(
                        List.of(id),
                        new PageableRequest(0, 50, "createTime", Sort.Direction.DESC))
                .getOrDefault(id, Collections.emptyList()));
        response.setEmojis(emojiService.findAllByPostIds(List.of(id))
                .getOrDefault(id, Collections.emptyList()));

        return response;
    }

    @Override
    public List<PostLogs> getAllPosts() {
        return postLogsRepository.findAll();
    }

    @Override
    public List<PostLogsResponse> findAllByUserId(UUID userId, PageableRequest request) {
        UserCache user = userCacheService.getUserCacheById(userId);
        if (user == null) return Collections.emptyList();

        List<PostLogsResponse> responses;
        List<UUID> postIds = redisUtil.getIdsFromZSet(
                DataCache.getPostOfUserKeyInRedis(userId),
                request.getPage(),
                request.getSize(),
                UUID::fromString
        );

        if (postIds.isEmpty()) {
            responses = postLogsRepository.findAllByUserId(userId, request.getPageableRequest());
        } else {
            responses = redisUtil.multiGetFromRedis(
                    StringUtil.convertCollectionElements(postIds, String.class),
                    PostLogsResponse.class);
        }

        if (responses.isEmpty()) return responses;

        Map<UUID, List<CommentResponse>> commentMap = commentsService.getCommentsByPostIds(postIds, new PageableRequest());
        Map<UUID, List<EmojiResponse>> emojiMap = emojiService.findAllByPostIds(postIds);

        for (PostLogsResponse post : responses) {
            post.setFirstName(user.getFirstName());
            post.setLastName(user.getLastName());
            post.setUsername(user.getUsername());
            post.setComments(commentMap.getOrDefault(post.getId(), Collections.emptyList()));
            post.setEmojis(emojiMap.getOrDefault(post.getId(), Collections.emptyList()));
        }

        return responses;
    }

    private String getKeyInRedis(UUID key) {
        return DataCache.getPostKeyInRedis(key);
    }

}
