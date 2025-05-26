package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ChatRoom;
import com.atimaby.chatserver.chat.domain.ReadStatus;
import com.atimaby.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}
