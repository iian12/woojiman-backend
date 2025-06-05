package com.dju.woojiman.domain.user.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @Tsid
    private String id;

    private String nickname;
    private String email;
    private String description;
    private int age;

    private String profileImageUrl;

    private boolean profileCompleted;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_oauth_accounts", joinColumns = @JoinColumn(name = "user_id"))
    private Set<OAuthAccount> oauthAccounts = new HashSet<>();

    private Role role;

    @Builder
    public Users(String nickname, String email, String description, int age, String profileImageUrl, Set<OAuthAccount> oauthAccounts, Role role, boolean profileCompleted) {
        this.nickname = nickname;
        this.email = email;
        this.description = description;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.oauthAccounts = oauthAccounts;
        this.role = role;
        this.profileCompleted = false;
    }
}
