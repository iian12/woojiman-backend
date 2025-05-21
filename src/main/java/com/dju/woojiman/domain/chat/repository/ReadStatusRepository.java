package com.dju.woojiman.domain.chat.repository;

import com.dju.woojiman.domain.chat.model.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, String> {

}
