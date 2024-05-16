package com.rise.controllers;

import com.rise.dto.BookingRequest;
import com.rise.entity.MealBooking;
import com.rise.service.MealBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private MealBookingService bookingService;

    @PostMapping
    public ResponseEntity<List<MealBooking>> bookMultipleDays(@RequestBody BookingRequest bookingRequest) {
        List<MealBooking> bookedMeals = bookingService.bookMultipleDays(bookingRequest.getUserId(), bookingRequest.getStartDate(), bookingRequest.getEndDate());
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
}

