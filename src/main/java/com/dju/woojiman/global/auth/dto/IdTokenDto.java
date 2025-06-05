package com.dju.woojiman.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IdTokenDto {

    private String idToken;

    public IdTokenDto(String idToken) {
        this.idToken = idToken;
    }
}
