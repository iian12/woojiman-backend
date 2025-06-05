package com.dju.woojiman.domain.user.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class OAuthAccount {

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    private String oauthId;

    @Builder
    public OAuthAccount(OAuth2Provider provider, String oauthId) {
        this.provider = provider;
        this.oauthId = oauthId;
    }
}
