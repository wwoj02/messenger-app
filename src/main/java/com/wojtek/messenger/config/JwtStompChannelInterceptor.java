package com.wojtek.messenger.config;

import com.wojtek.messenger.conversation.ConversationMemberRepository;
import com.wojtek.messenger.security.JwtService;
import com.wojtek.messenger.user.CustomUserDetailsService;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class JwtStompChannelInterceptor implements ChannelInterceptor {
    private static final String CONVERSATION_TOPIC_PREFIX = "/topic/conversations/";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message,
                StompHeaderAccessor.class
        );

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUserName(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.validateToken(token, userDetails)) {
                throw new AccessDeniedException("Invalid JWT token");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            accessor.setUser(authentication);

        } catch (Exception exception) {
            throw new AccessDeniedException("Invalid JWT token");
        }
    }

    private void authorizeSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null || !destination.startsWith(CONVERSATION_TOPIC_PREFIX)) {
            throw new AccessDeniedException("Invalid subscription destination");
        }

        Integer conversationId;

        try {
            String conversationIdValue = destination.substring(CONVERSATION_TOPIC_PREFIX.length());
            conversationId = Integer.valueOf(conversationIdValue);
        } catch (NumberFormatException exception) {
            throw new AccessDeniedException("Invalid conversation ID");
        }

        Principal principal = accessor.getUser();

        if (principal == null) {
            throw new AccessDeniedException("Unauthenticated WebSocket connection");
        }

        User user = userRepository.findByUsername(principal.getName());

        if (user == null || !conversationMemberRepository
                .existsByConversation_IdAndUser_Id(conversationId, user.getId())) {
            throw new AccessDeniedException(
                    "You are not a participant of this conversation"
            );
        }
    }
}
