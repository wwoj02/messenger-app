package com.wojtek.messenger.message;

import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import com.wojtek.messenger.user.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages")
    public MessageResponse sendMessage(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody MessageRequest request) {
        return messageService.sendMessage(principal.getUsername(), request);
    }

    @GetMapping("/messages/{conversationId}")
    public List<MessageResponse> getMessages(
            @PathVariable Integer conversationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return messageService.getMessages(conversationId, principal.getUsername());
    }
}
