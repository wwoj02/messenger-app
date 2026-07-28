package com.wojtek.messenger.message;

import com.wojtek.messenger.conversation.Conversation;
import com.wojtek.messenger.conversation.ConversationRepository;
import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserMapper;
import com.wojtek.messenger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final UserMapper userMapper;

    public Message fromRequestToMessage(MessageRequest request) {
        Message msg = new Message();

        Conversation conversation = conversationRepository.findById(
                request.conversationId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        User user = userRepository.findById(
                request.senderId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        msg.setConversation(conversation);
        msg.setSender(user);
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
