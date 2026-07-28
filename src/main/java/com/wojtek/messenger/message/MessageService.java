package com.wojtek.messenger.message;

import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageResponse sendMessage(MessageRequest request) {
        Message msg = messageMapper.fromRequestToMessage(request);
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
