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
        List<MealBooking> bookedMeals = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            MealBooking mealBooking = new MealBooking();
            mealBooking.setUserId(userId);
            mealBooking.setStartDate(currentDate);
            mealBooking.setEndDate(currentDate);
            UUID token = UUID.randomUUID();
            mealBooking.setToken(token);
            MealBooking savedMealBooking = bookingRepository.save(mealBooking);

            Token tokenEntity = new Token();
            tokenEntity.setMealId(savedMealBooking.getMealId());
            tokenEntity.setToken(token);
            tokenRepository.save(tokenEntity);

            bookedMeals.add(savedMealBooking);
            currentDate = currentDate.plusDays(1);
        }

        return bookedMeals;
    }

    public void cancelMeal(Long mealId) {
        MealBooking mealBooking = bookingRepository.findById(mealId).orElseThrow(() -> new RuntimeException("Meal booking not found"));
        mealBooking.setCanceled(true);
        bookingRepository.save(mealBooking);
    }

    public List<MealBooking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<MealBooking> getAllBookings() {
        return bookingRepository.findAll();
    }
}

