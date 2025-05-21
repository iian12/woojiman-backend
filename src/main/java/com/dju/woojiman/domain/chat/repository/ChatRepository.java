package com.dju.woojiman.domain.chat.repository;

import com.dju.woojiman.domain.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {

}
