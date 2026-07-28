package com.wojtek.messenger.conversation.dto;

import com.wojtek.messenger.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ConversationResponse(
        Integer id,
        List<UserResponse> participants,
        String name,
        LocalDateTime createdAt) {
}
