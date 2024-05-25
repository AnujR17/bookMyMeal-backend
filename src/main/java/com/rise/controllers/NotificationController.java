package com.rise.controllers;

import com.rise.dto.NotificationDTO;
import com.rise.dto.NotificationFetchDTO;
import com.rise.entity.Notification;
import com.rise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotification(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@RequestBody NotificationFetchDTO notificationFetchDTO) {
        String userId = notificationFetchDTO.getUserId();
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return ResponseEntity.ok().body(notifications);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotifications(@RequestBody NotificationDTO notificationDTO) {
        notificationService.deleteNotifications(notificationDTO.getUserId());
        return ResponseEntity.noContent().build();
    }
}
