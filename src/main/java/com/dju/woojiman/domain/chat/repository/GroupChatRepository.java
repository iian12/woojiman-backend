package com.dju.woojiman.domain.chat.repository;

import com.dju.woojiman.domain.chat.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRepository extends JpaRepository<GroupChat, String> {

}
