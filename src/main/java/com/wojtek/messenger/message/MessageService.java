package com.wojtek.messenger.message;

import com.wojtek.messenger.conversation.ConversationMemberRepository;
import com.wojtek.messenger.message.dto.MessageRequest;
import com.wojtek.messenger.message.dto.MessageResponse;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserRepository;
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
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;

    public MessageResponse sendMessage(String username, MessageRequest request) {
        User sender = userRepository.findByUsername(username);

        Message msg = messageMapper.fromRequestToMessage(request, sender);

        boolean isMember = conversationMemberRepository
                .existsByConversation_IdAndUser_Id(
                        request.conversationId(),
                        sender.getId());

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not a participant of this conversation!");
        }

        messageRepository.save(msg);
        return messageMapper.fromMessageToResponse(msg);
    }

    public List<MessageResponse> getMessages(Integer conversationId, String username) {
        User user = userRepository.findByUsername(username);

        boolean isMember = conversationMemberRepository.existsByConversation_IdAndUser_Id(conversationId, user.getId());

        if(isMember) {
            List<Message> messages = messageRepository.findByConversation_Id(conversationId);
            List<MessageResponse> messageResponses = new ArrayList<>();

            for (Message msg : messages) {
                messageResponses.add(messageMapper.fromMessageToResponse(msg));
            }

            return messageResponses;
        }

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No permission"
        );
    }
}
