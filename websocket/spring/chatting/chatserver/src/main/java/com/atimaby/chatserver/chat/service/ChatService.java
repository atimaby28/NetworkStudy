package com.atimaby.chatserver.chat.service;

import com.atimaby.chatserver.chat.repository.ChatMessageRepository;
import com.atimaby.chatserver.chat.repository.ChatParticipantRepository;
import com.atimaby.chatserver.chat.repository.ChatRoomRepository;
import com.atimaby.chatserver.chat.repository.ReadStatusRepository;
import com.atimaby.chatserver.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ReadStatusRepository readStatusRepository;

    public ChatService(MemberRepository memberRepository, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, ChatParticipantRepository chatParticipantRepository, ReadStatusRepository readStatusRepository) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.readStatusRepository = readStatusRepository;
    }
}
