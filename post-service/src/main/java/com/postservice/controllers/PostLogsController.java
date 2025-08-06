package com.postservice.controllers;

import com.core.dto.response.DataResponse;
import com.core.utils.HttpServletRequestUtil;
import com.postservice.dto.request.PostLogsRequest;
import com.postservice.dto.response.PostLogsResponse;
import com.postservice.entities.PostLogs;
import com.postservice.services.abs.PostLogsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLogsController {

    PostLogsService postLogsService;

    @PostMapping
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse createPost(@RequestBody PostLogsRequest request) {
        return new DataResponse(postLogsService.createPost(request, HttpServletRequestUtil.getCurrentUserId()));
    }

    @PutMapping("/{id}")
    public DataResponse updatePost(@PathVariable UUID id, @RequestBody PostLogsRequest request) {

        return new DataResponse(postLogsService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public DataResponse deletePost(@PathVariable UUID id) {
        postLogsService.deletePost(id);
        return DataResponse.builder()
                .message("Xóa thành công!")
                .build();
    }

    @GetMapping("/{id}")
    public PostLogsResponse getPostById(@PathVariable UUID id) {
        return postLogsService.getPostById(id);
    }

    @GetMapping
    public List<PostLogs> getAllPosts() {
        return postLogsService.getAllPosts();
    }
    
    @GetMapping("/user/{userId}")
    public DataResponse getAllPostsByUserId(@PathVariable UUID userId) {
        List<PostLogs> posts = postLogsService.findAllByUserId(userId);

        return new DataResponse(posts != null ? posts : new ArrayList<>());
    }

}
