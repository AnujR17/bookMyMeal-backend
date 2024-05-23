package com.rise.controllers;

import com.rise.dto.MealBookingRequest;
import com.rise.entity.MealBooking;
import com.rise.service.MealBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/meals")
public class MealController {
    @Autowired
    private MealBookingService mealBookingService;

    @PostMapping
    public ResponseEntity<List<MealBooking>> bookMultipleDays(@RequestBody MealBookingRequest mealBookingRequest) {
        List<MealBooking> bookedMeals = mealBookingService.bookMultipleDays(
                mealBookingRequest.getUserId(),
                mealBookingRequest.getStartDate(),
                mealBookingRequest.getEndDate()
        );
        return ResponseEntity.ok(bookedMeals);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<MealBooking>> getMealsByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<MealBooking> meals = mealBookingService.getAllBookings().stream()
                .filter(meal -> meal.getDate().equals(date))
                .toList();
        return ResponseEntity.ok(meals);
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<String> cancelMeal(@PathVariable Long mealId) {
        mealBookingService.cancelMeal(mealId);
        return ResponseEntity.ok("Meal canceled successfully");
    }
}
