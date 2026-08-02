package com.wojtek.messenger.conversation;

import com.wojtek.messenger.conversation.dto.ConversationRequest;
import com.wojtek.messenger.conversation.dto.ConversationResponse;
import com.wojtek.messenger.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("/conversations/{id}")
    public ConversationResponse getConversation(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return conversationService.getConversation(id, principal.getUsername());
    }

    @PostMapping("/conversations")
    public ConversationResponse createConversation(
            @RequestBody ConversationRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return conversationService.createConversation(request, principal.getUsername());
    }

}
