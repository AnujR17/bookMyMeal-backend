package com.rise.repository;

import com.rise.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Transactional
    void deleteByUserId(String userId);

    List<Notification> findByUserId(String userId);
}