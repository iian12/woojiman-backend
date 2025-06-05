package com.dju.woojiman.domain.user.repository;

import com.dju.woojiman.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
}
