package com.rise.service;

import com.rise.dto.NotificationDTO;
import com.rise.entity.Notification;
import com.rise.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(NotificationDTO notificationDTO) {
        String message = getNotificationMessage(notificationDTO.getStartDate(), notificationDTO.getEndDate());
        Notification notification = new Notification(
                notificationDTO.getUserId(),
                notificationDTO.getUserName(),
                message,
                LocalDateTime.now()
        );
        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void deleteNotifications(String userId) {
        notificationRepository.deleteByUserId(userId);
    }

    private String getNotificationMessage(LocalDate startDate, LocalDate endDate) {
        if (startDate.equals(endDate)) {
            return "Meal has been booked for " + startDate + ".";
        } else {
            return "Your meal has been booked from " + startDate + " to " + endDate + ".";
        }
    }
}