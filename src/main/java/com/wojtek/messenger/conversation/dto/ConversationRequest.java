package com.wojtek.messenger.conversation.dto;

import java.util.List;

public record ConversationRequest(
        List<Integer> receivers
) {
}