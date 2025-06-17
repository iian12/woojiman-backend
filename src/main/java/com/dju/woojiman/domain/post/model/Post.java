package com.dju.woojiman.domain.post.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @Tsid
    private String id;

    private String title;
    private String content;

    private String ownerId;

    @Builder
    public Post(String title, String content, String ownerId) {
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
    }
}
