package com.wojtek.messenger.message;

import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages")
    public void sendMessage(
            @Valid MessageRequest request,
            Principal principal
    ) {
        MessageResponse response = messageService.sendMessage(
                principal.getName(),
                request
        );

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + response.conversationId(),
                response
        );
    }
}
