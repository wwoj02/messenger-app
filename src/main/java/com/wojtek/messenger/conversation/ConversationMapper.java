package com.wojtek.messenger.conversation;

import com.wojtek.messenger.conversation.dto.ConversationResponse;
import com.wojtek.messenger.user.UserMapper;
import com.wojtek.messenger.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConversationMapper {
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserMapper userMapper;

    public ConversationResponse toConversationResponse(Conversation conversation) {
        List<UserResponse> participants = conversationMemberRepository
                .findByConversation_Id(conversation.getId())
                .stream()
                .map(ConversationMember::getUser)
                .map(userMapper::toUserResponse)
                .toList();

        return new ConversationResponse(
                conversation.getId(),
                participants,
                conversation.getName(),
                conversation.getCreatedAt());
    }
}
