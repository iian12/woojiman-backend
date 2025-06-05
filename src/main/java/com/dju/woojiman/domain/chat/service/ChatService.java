package com.dju.woojiman.domain.chat.service;

import com.dju.woojiman.domain.chat.dto.ChatMessageDto;
import com.dju.woojiman.domain.chat.dto.CreateGroupChat;
import com.dju.woojiman.domain.chat.model.ChatMessage;
import com.dju.woojiman.domain.chat.model.GroupChat;
import com.dju.woojiman.domain.chat.model.PrivateChat;
import com.dju.woojiman.domain.chat.repository.*;
import com.dju.woojiman.domain.user.repository.UserRepository;
import com.dju.woojiman.domain.user.model.Users;
import com.dju.woojiman.global.auth.CustomUserDetail;
import com.dju.woojiman.global.config.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final RedisPubSubService redisPubSubService;
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatRepository privateChatRepository;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       ReadStatusRepository readStatusRepository,
                       UserRepository userRepository, RedisPubSubService redisPubSubService, GroupChatRepository groupChatRepository, PrivateChatRepository privateChatRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.redisPubSubService = redisPubSubService;
        this.groupChatRepository = groupChatRepository;
        this.privateChatRepository = privateChatRepository;
    }

    public String createGroupChatRoom(CustomUserDetail customUserDetail, CreateGroupChat createGroupChat) {

        Users user = customUserDetail.getUser();

        GroupChat groupChat = GroupChat.builder()
                .name(createGroupChat.getName())
                .description(createGroupChat.getDescription())
                .ownerId(user.getId())
                .build();
        groupChatRepository.save(groupChat);

        return groupChat.getId();
    }

    public String createPrivateChatRoom(CustomUserDetail customUserDetail, String targetUserId) {
        Users user = customUserDetail.getUser();
        Users targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found"));

        PrivateChat privateChat = PrivateChat.builder()
                .user1Id(user.getId())
                .user2Id(targetUserId)
                .user1Nickname(user.getNickname())
                .user2Nickname(targetUser.getNickname())
                .build();
        privateChatRepository.save(privateChat);

        return privateChat.getId();
    }

    public void sendMessage(String roomId, ChatMessageDto chatMessageDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        chatMessageDto.setRoomId(roomId);
        String message = objectMapper.writeValueAsString(chatMessageDto);

        Users user = userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(roomId)
                .content(chatMessageDto.getMessage())
                .userId(user.getId())
                .build();
        chatMessageRepository.save(chatMessage);
        redisPubSubService.publish("chat", message);
    }
}
