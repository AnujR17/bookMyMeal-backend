package com.rise.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class MealBookingRequest {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
}
