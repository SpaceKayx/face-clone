package com.postservice.controllers;

import com.core.dto.response.DataResponse;
import com.core.dto.response.PageableRequest;
import com.core.utils.HttpServletRequestUtil;
import com.postservice.dto.request.CommentRequest;
import com.postservice.services.abs.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping
    public DataResponse create(@RequestBody CommentRequest request) {
        request.setUserId(HttpServletRequestUtil.getCurrentUserId());

        return new DataResponse(commentService.createComment(request));
    }

    @PutMapping("/{id}")
    public DataResponse update(@PathVariable long id, @RequestBody CommentRequest request) {

        return new DataResponse(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public DataResponse delete(@PathVariable long id) {
        commentService.deleteComment(id);
        return DataResponse.builder()
                .message("Xóa thành công!")
                .build();
    }

    @GetMapping("/more-comment/{postId}")
    public DataResponse moreComment(@PathVariable UUID postId, @RequestParam PageableRequest request) {
        return new DataResponse(
                commentService.getCommentsByPostIds(List.of(postId), request)
                        .get(postId));
    }

}
