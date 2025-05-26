package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ChatParticipant;
import com.atimaby.chatserver.chat.domain.ChatRoom;
import com.atimaby.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatParticipant> findAllByMember(Member member);
}
