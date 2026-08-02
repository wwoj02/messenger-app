package com.wojtek.messenger.message;

import com.wojtek.messenger.conversation.Conversation;
import com.wojtek.messenger.conversation.ConversationRepository;
import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final ConversationRepository conversationRepository;
    private final UserMapper userMapper;

    public Message fromRequestToMessage(MessageRequest request, User sender) {
        Message msg = new Message();

        Conversation conversation = conversationRepository.findById(
                request.conversationId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        msg.setConversation(conversation);
        msg.setSender(sender);
        msg.setContent(request.content());

        return msg;
    }

    public MessageResponse fromMessageToResponse(Message msg) {
        return new MessageResponse(
                msg.getId(),
                msg.getConversation().getId(),
                userMapper.toUserResponse(msg.getSender()),
                msg.getContent(),
                msg.getSentAt()
        );
    }
}
