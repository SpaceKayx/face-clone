package com.postservice.controllers;

import com.core.constants.FConstants;
import com.core.dto.response.DataResponse;
import com.postservice.dto.request.PostLogsRequest;
import com.postservice.entities.PostLogs;
import com.postservice.services.abs.PostLogsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLogsController {

    PostLogsService postLogsService;
    HttpServletRequest request;

    @PostMapping
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse createPost(@RequestBody PostLogsRequest request) {
        return new DataResponse(postLogsService.createPost(request, this.getUserIdFromHeader()));
    }

    @PutMapping("/{id}")
    public DataResponse updatePost(@PathVariable String id, @RequestBody PostLogsRequest request) {

        return new DataResponse(postLogsService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public DataResponse deletePost(@PathVariable String id) {
        postLogsService.deletePost(id);
        return DataResponse.builder()
                .message("Xóa thành công!")
                .build();
    }

    @GetMapping("/{id}")
    public PostLogs getPostById(@PathVariable String id) {
        return postLogsService.getPostById(id);
    }

    @GetMapping
    public List<PostLogs> getAllPosts() {
        return postLogsService.getAllPosts();
    }
    
    @GetMapping("/user/{userId}")
    public DataResponse getAllPostsByUserId(@PathVariable String userId) {
        List<PostLogs> posts = postLogsService.findAllByUserId(userId);

        return new DataResponse(posts != null ? posts : new ArrayList<>());
    }

    private String getUserIdFromHeader() {
        return request.getHeader(FConstants.USER_ID);
    }

}
