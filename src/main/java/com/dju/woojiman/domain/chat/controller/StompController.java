package com.dju.woojiman.domain.chat.controller;

import com.dju.woojiman.domain.chat.dto.ChatMessageDto;
import com.dju.woojiman.domain.chat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    private final ChatService chatService;

    public StompController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatMessageDto chatMessageDto)
            throws JsonProcessingException {
        chatService.sendMessage(roomId, chatMessageDto);
    }
}
