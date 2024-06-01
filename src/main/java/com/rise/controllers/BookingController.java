package com.rise.controllers;

import com.rise.dto.BookingRequest;
import com.rise.entity.MealBooking;
import com.rise.service.MealBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private MealBookingService bookingService;

    @PostMapping
    public ResponseEntity<List<MealBooking>> bookMultipleDays(@RequestBody BookingRequest bookingRequest) {
        Long userId = bookingRequest.getUserId();
        LocalDate startDate = bookingRequest.getStartDate();
        LocalDate endDate = bookingRequest.getEndDate();

        if (userId == null || startDate == null || endDate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<MealBooking> bookedMeals = bookingService.bookMultipleDays(userId, startDate, endDate);
        return ResponseEntity.ok(bookedMeals);
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<String> cancelMeal(@PathVariable Long mealId) {
        bookingService.cancelMeal(mealId);
        return ResponseEntity.ok("Meal booking canceled successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MealBooking>> getBookingsByUser(@PathVariable Long userId) {
        List<MealBooking> userBookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(userBookings);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MealBooking>> getAllBookings() {
        List<MealBooking> allBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/meal/{mealId}")
    public MealBooking getMealById(@PathVariable Long mealId) {
        return bookingService.getMealById(mealId);
    }
}

