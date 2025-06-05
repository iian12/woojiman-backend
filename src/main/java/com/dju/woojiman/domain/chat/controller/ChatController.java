package com.dju.woojiman.domain.chat.controller;

import com.dju.woojiman.domain.chat.service.ChatService;
import com.dju.woojiman.domain.chat.dto.CreateGroupChat;
import com.dju.woojiman.global.auth.CustomUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroupChat(@AuthenticationPrincipal CustomUserDetail user,
                                             CreateGroupChat createGroupChat) {
        return ResponseEntity.ok(chatService.createGroupChatRoom(user, createGroupChat));
    }

    @PostMapping("/create-private")
    public ResponseEntity<?> createPrivateChat(@AuthenticationPrincipal CustomUserDetail user,
                                               String targetUserId) {
        return ResponseEntity.ok(chatService.createPrivateChatRoom(user, targetUserId));
    }
}
