package com.rise.service;


import com.rise.entity.MealBooking;
import com.rise.entity.Token;
import com.rise.repository.MealBookingRepository;
import com.rise.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MealBookingService {
    @Autowired
    private MealBookingRepository bookingRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public List<MealBooking> bookMultipleDays(Long userId, LocalDate startDate, LocalDate endDate) {
        List<MealBooking> bookings = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate.plusDays(1))) {
            MealBooking booking = new MealBooking();
            booking.setUserId(userId);
            booking.setStartDate(currentDate);
            booking.setEndDate(currentDate);
            MealBooking savedBooking = bookingRepository.save(booking);
            bookings.add(savedBooking);
            currentDate = currentDate.plusDays(1);
        }
        return bookings;
    }

    public void cancelBooking(Long bookingId) {
        MealBooking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setCanceled(true);
        bookingRepository.save(booking);
    }

    public List<MealBooking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<MealBooking> getAllBookings() {
        return bookingRepository.findAll();
    }
}

