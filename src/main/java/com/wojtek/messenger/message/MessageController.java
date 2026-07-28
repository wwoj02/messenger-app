package com.wojtek.messenger.message;

import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages")
    public MessageResponse sendMessage(@RequestBody MessageRequest request) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/messages/{conversationId}")
    public List<MessageResponse> getMessage(@PathVariable Integer conversationId) {
        return messageService.getMessages(conversationId);
    }
}
