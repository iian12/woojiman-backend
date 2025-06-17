package com.dju.woojiman.domain.post.service;

import com.dju.woojiman.domain.post.dto.PostDto;
import com.dju.woojiman.domain.post.dto.PostListResDto;
import com.dju.woojiman.domain.post.model.Post;
import com.dju.woojiman.domain.post.repository.PostRepository;
import com.dju.woojiman.domain.user.model.Users;
import com.dju.woojiman.global.auth.CustomUserDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public String createPost(CustomUserDetail userDetail, PostDto postDto) {
        Users user = userDetail.getUser();

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .ownerId(user.getId())
                .build();

        postRepository.save(post);
        return post.getId();
    }

}
