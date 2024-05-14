package com.rise.controllers;

import com.rise.entity.MealBooking;
import com.rise.service.MealBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class MealBookingController {

    private final MealBookingService mealBookingService;

    @Autowired
    public MealBookingController(MealBookingService mealBookingService) {
        this.mealBookingService = mealBookingService;
    }

    @PostMapping
    public ResponseEntity<String> bookMeal(@RequestBody MealBooking mealBooking) {
        mealBookingService.bookMeal(mealBooking);
        return ResponseEntity.ok("Meal booked successfully.");
    }
}