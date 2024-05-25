package com.rise.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificationDTO {
    private String userId;
    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
}
