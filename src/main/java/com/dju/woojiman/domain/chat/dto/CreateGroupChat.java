package com.dju.woojiman.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGroupChat {
    private String name;
    private String description;

    public CreateGroupChat(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
