package com.atimaby.chatserver.chat.repository;

import com.atimaby.chatserver.chat.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
}
