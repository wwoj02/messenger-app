package com.wojtek.messenger.message;

import com.wojtek.messenger.conversation.Conversation;
import com.wojtek.messenger.conversation.ConversationMemberRepository;
import com.wojtek.messenger.conversation.ConversationRepository;
import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;

    public MessageResponse sendMessage(MessageRequest request) {
        Message msg = messageMapper.fromRequestToMessage(request);

        boolean isMember = conversationMemberRepository
                .existsByConversation_IdAndUser_Id(request.conversationId(), request.senderId());

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not a participant of this conversation!");
        }

        messageRepository.save(msg);
        return messageMapper.fromMessageToResponse(msg);
    }

    public List<MessageResponse> getMessages(Integer conversationId) {
        List<Message> messages = messageRepository.findByConversation_Id(conversationId);
        List<MessageResponse> messageResponses = new ArrayList<>();

        for (Message msg : messages) {
            messageResponses.add(messageMapper.fromMessageToResponse(msg));
        }

        return messageResponses;
    }
}
