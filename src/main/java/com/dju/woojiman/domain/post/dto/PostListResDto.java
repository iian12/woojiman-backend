package com.dju.woojiman.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListResDto {

    private String postId;
    private String title;

    @Builder
    public PostListResDto(String postId, String title) {
        this.postId = postId;
        this.title = title;
    }
}
