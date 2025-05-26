package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ChatMessage;
import com.atimaby.chatserver.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByCreatedTimeAsc(ChatRoom chatRoom);
}
