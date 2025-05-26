package com.atimaby.chatserver.chat.service;

import com.atimaby.chatserver.chat.domain.ChatMessage;
import com.atimaby.chatserver.chat.domain.ChatParticipant;
import com.atimaby.chatserver.chat.domain.ChatRoom;
import com.atimaby.chatserver.chat.domain.ReadStatus;
import com.atimaby.chatserver.chat.dto.ChatMessageDto;
import com.atimaby.chatserver.chat.dto.ChatRoomListDto;
import com.atimaby.chatserver.chat.dto.MyChatListResDto;
import com.atimaby.chatserver.chat.repository.ChatMessageRepository;
import com.atimaby.chatserver.chat.repository.ChatParticipantRepository;
import com.atimaby.chatserver.chat.repository.ChatRoomRepository;
import com.atimaby.chatserver.chat.repository.ReadStatusRepository;
import com.atimaby.chatserver.member.domain.Member;
import com.atimaby.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void saveMessage(Long roomId, ChatMessageDto chatMessageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room can not be found !"));

        // 보낸사람 조회
        Member sender = memberRepository.findByEmail(chatMessageDto.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("Member can not be found !"));

        // 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(chatMessageDto.getMessage())
                .build();

        chatMessageRepository.save(chatMessage);

        // 사용자별로 읽음여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        for (ChatParticipant cp : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .member(cp.getMember())
                    .chatMessage(chatMessage)
                    .isRead(cp.getMember().equals(sender))
                    .build();

            readStatusRepository.save(readStatus);
        }
    }

    public void createGroupRoom(String chatRoomName) {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("Member can not be found !"));

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .isGroupChat("Y")
                .build();

        chatRoomRepository.save(chatRoom);

        // 채팅 참여자로 개설자를 추가
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatRoomListDto> getGroupChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomListDto chatRoomListDto = ChatRoomListDto.builder()
                    .roomId(chatRoom.getId())
                    .roomName(chatRoom.getName())
                    .build();

            chatRoomListDtos.add(chatRoomListDto);
        }

        return chatRoomListDtos;
    }

    public void addParticipantToGroupChat(Long roomId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Chatting room can not be found"));

        // member 조회
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("Member can not be found"));

        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("그룹채팅이 아닙니다.");
        }

        // 이미 참여하고 있는지 확인
        Optional<ChatParticipant> chatParticipant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);

        if (chatParticipant.isEmpty())
            addParticipantToRoom(chatRoom, member);

    }

    // ChatParticipant 객체 생성 후 저장
    public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room can not be found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("Member can not be found"));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        boolean check = false;

        for (ChatParticipant c : chatParticipants) {
            if (c.getMember().equals(member)) {
                check = true;
            }
        }

        // 내가 해당 채팅방의 참여자가 아닐경우 에러
        if (!check) throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");

        // 특정 Room에 대한 Message 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();

        for (ChatMessage cm : chatMessages) {
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(cm.getContent())
                    .senderEmail(cm.getMember().getEmail())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }

        return chatMessageDtos;
    }

    public boolean isRoomParticipant(String email, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room can not be found"));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Member can not be found"));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        for (ChatParticipant cp : chatParticipants) {
            if (cp.getMember().equals(member)) {
                return true;
            }
        }

        return false;
    }

    public void messageRead(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room can not be found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("Member cannot be found"));

        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom, member);

        for (ReadStatus rs : readStatuses) {
            rs.updateIsRead(true);
        }
    }

    public List<MyChatListResDto> getMyChatRooms() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member cannot be found"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListResDto> chatListResDtos = new ArrayList<>();

        for (ChatParticipant cp : chatParticipants) {
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(cp.getChatRoom(), member);
            MyChatListResDto dto = MyChatListResDto.builder()
                    .roomId(cp.getChatRoom().getId())
                    .roomName(cp.getChatRoom().getName())
                    .isGroupChat(cp.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            chatListResDtos.add(dto);
        }
        return chatListResDtos;
    }

    public void leaveGroupChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room can not be found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("Member can not be found"));

        if (chatRoom.getIsGroupChat().equals("N")) {
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }

        ChatParticipant cp = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).orElseThrow(() -> new EntityNotFoundException("참여자를 찾을 수 없습니다."));
        chatParticipantRepository.delete(cp);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if (chatParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public Long getOrCreatePrivateRoom(Long otherMemberId) {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException("member cannot be found"));
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(()->new EntityNotFoundException("member cannot be found"));

        // 나와 상대방이 1:1채팅에 이미 참석하고 있다면 해당 roomId return
        Optional<ChatRoom> chatRoom = chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId());

        if(chatRoom.isPresent()){
            return chatRoom.get().getId();
        }

        // 만약에 1:1채팅방이 없을경우 기존 채팅방 개설
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(member.getName() + "-" + otherMember.getName())
                .build();

        chatRoomRepository.save(newRoom);

        // 두사람 모두 참여자로 새롭게 추가
        addParticipantToRoom(newRoom, member);
        addParticipantToRoom(newRoom, otherMember);

        return newRoom.getId();
    }
}
