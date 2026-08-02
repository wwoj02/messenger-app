package com.wojtek.messenger.conversation;

import com.wojtek.messenger.conversation.dto.ConversationRequest;
import com.wojtek.messenger.conversation.dto.ConversationResponse;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final ConversationMapper conversationMapper;

    public ConversationResponse createConversation(ConversationRequest request, String username) {
        Conversation conversation = new Conversation();
        conversation.setCreatedAt(LocalDateTime.now());

        User sender = userRepository.findByUsername(username);

        if (sender == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user not found"
            );
        }

        List<Integer> allMembers = new ArrayList<>(request.receivers());
        allMembers.add(sender.getId());


        if (request.receivers().size() == 1) {
            conversation.setType(ConversationType.PRIVATE);
            Integer userId = request.receivers().getFirst();
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "User not found"
                    )
            );
            conversation.setName(user.getUsername());
        }
        else {
            conversation.setType(ConversationType.GROUP);
        }

        conversationRepository.save(conversation);

        for (Integer memberId : allMembers) {
            ConversationMember member = new ConversationMember();

            User user = userRepository.findById(memberId).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "User not found"
                    ));

            member.setUser(user);


            member.setConversation(conversation);
            member.setJoinedAt(LocalDateTime.now());

            conversationMemberRepository.save(member);
        }

        return conversationMapper.toConversationResponse(conversation);
    }

    public ConversationResponse getConversation(Integer id, String username) {
        User user = userRepository.findByUsername(username);
        boolean isMember = conversationMemberRepository.existsByConversation_IdAndUser_Id(id, user.getId());

        if (isMember) {
            Conversation conversation = conversationRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Conversation not found"
                    ));

            return conversationMapper.toConversationResponse(conversation);
        }

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No permissions"
        );
    }
}
