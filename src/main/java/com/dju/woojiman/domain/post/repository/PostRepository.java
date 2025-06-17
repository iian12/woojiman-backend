package com.dju.woojiman.domain.post.repository;

import com.dju.woojiman.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}
