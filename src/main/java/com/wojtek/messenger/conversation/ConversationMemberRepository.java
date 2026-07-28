package com.wojtek.messenger.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Integer> {
    List<ConversationMember> findByConversation_Id(Integer conversationId);
}
