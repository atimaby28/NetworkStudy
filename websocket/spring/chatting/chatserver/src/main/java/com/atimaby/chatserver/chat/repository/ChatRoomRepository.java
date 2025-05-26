package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ChatParticipant;
import com.atimaby.chatserver.chat.domain.ChatRoom;
import com.atimaby.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByIsGroupChat(String isGroupChat);

}
