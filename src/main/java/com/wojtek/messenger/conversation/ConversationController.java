package com.wojtek.messenger.conversation;

import com.wojtek.messenger.conversation.dto.ConversationRequest;
import com.wojtek.messenger.conversation.dto.ConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("/conversations/{id}")
    public ConversationResponse getConversation(@PathVariable Integer id) {
        return conversationService.getConversation(id);
    }

    @PostMapping("/conversations")
    public ConversationResponse createConversation(@RequestBody ConversationRequest request) {
        return conversationService.createConversation(request);
    }

}
