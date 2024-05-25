package com.rise.repository;

import com.rise.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByUserId(String userId);

    List<Notification> findByUserId(String userId);
}