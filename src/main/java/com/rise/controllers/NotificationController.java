package com.rise.controllers;

import com.rise.dto.NotificationDTO;
import com.rise.entity.Notification;
import com.rise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationDTO notificationDTO) {
        logger.info("Received request to create notification: " + notificationDTO);
        notificationService.createNotification(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@RequestParam String userId) {
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return ResponseEntity.ok().body(notifications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        logger.info("Received request to delete notification with id: " + id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotifications(@RequestParam String userId) {
        logger.info("Received request to delete notifications for user: " + userId);
        notificationService.deleteNotifications(userId);
        return ResponseEntity.noContent().build();
    }
}
