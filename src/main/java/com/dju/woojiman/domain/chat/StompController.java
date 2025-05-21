package com.dju.woojiman.domain.chat;

import com.dju.woojiman.global.config.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    private final RedisPubSubService pubSubService;

    public StompController(RedisPubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatMessageDto chatMessageDto)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        chatMessageDto.setRoomId(roomId);
        String message = objectMapper.writeValueAsString(chatMessageDto);
        pubSubService.publish("chat", message);
    }
}
