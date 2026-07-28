package com.wojtek.messenger.message.dto;

import com.wojtek.messenger.user.dto.UserResponse;

import java.time.LocalDateTime;

public record MessageResponse(
        Integer id,
        Integer conversationId,
        UserResponse sender,
        String content,
        LocalDateTime sentAt
) {
}
