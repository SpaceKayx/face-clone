package com.postservice.controllers;

import com.core.dto.response.DataResponse;
import com.core.utils.HttpServletRequestUtil;
import com.postservice.dto.request.CommentEmojiRequest;
import com.postservice.services.abs.CommentEmojiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment-emoji")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentEmojiController {

    CommentEmojiService commentEmojiService;

    @PostMapping
    public DataResponse create(@RequestBody CommentEmojiRequest request) {
        return new DataResponse(commentEmojiService.createEmoji(request, HttpServletRequestUtil.getCurrentUserId()));
    }

    @PutMapping()
    public DataResponse update(@RequestBody CommentEmojiRequest request) {
        return new DataResponse(commentEmojiService.updateEmoji(request, HttpServletRequestUtil.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    public DataResponse delete(@PathVariable long id) {
        commentEmojiService.deleteEmoji(id);
        return DataResponse.builder()
                .message("Xóa thành công!")
                .build();
    }

}
