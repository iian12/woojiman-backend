package com.dju.woojiman.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LinkInfo {

    private String email;
    private String provider;
    private String oauthId;

    public LinkInfo(String email, String provider, String oauthId) {
        this.email = email;
        this.provider = provider;
        this.oauthId = oauthId;
    }
}
