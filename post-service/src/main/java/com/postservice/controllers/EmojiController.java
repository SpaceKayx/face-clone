package com.postservice.controllers;

import com.core.dto.response.DataResponse;
import com.core.utils.HttpServletRequestUtil;
import com.postservice.dto.request.EmojiRequest;
import com.postservice.services.abs.EmojiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/emoji")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmojiController {

    EmojiService emojiService;

    @PutMapping
    public DataResponse createOrUpdate(@RequestBody EmojiRequest request) {
        emojiService.createOrUpdateEmoji(request, HttpServletRequestUtil.getCurrentUserId());
        return new DataResponse();
    }

    @DeleteMapping("/{postId}")
    public DataResponse delete(@PathVariable UUID postId) {
        emojiService.deleteEmoji(postId, HttpServletRequestUtil.getCurrentUserId());

        return DataResponse.builder()
                .message("Xóa thành công!")
                .build();
    }

}
