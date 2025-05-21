package com.dju.woojiman.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    @JsonIgnore
    private String roomId;
    private String messageId;
    private String message;

    public ChatMessageDto(String roomId, String messageId, String message) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.message = message;
    }
}
