package com.wojtek.messenger.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull Integer conversationId,
        @NotBlank String content
) {
}
