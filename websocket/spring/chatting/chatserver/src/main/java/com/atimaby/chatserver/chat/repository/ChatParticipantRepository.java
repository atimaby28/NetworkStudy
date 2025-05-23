package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
}
