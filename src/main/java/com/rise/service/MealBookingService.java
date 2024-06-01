package com.rise.service;

import com.rise.entity.MealBooking;
import com.rise.repository.MealBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MealBookingService {

    @Autowired
    private MealBookingRepository mealBookingRepository;

    // Define your holidays here
    private static final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2024, 1, 1),  // New Year's Day
            LocalDate.of(2024, 12, 25) // Christmas
            // Add more holidays as needed
    );

    public List<MealBooking> bookMultipleDays(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Start date must be at least tomorrow.");
        }

        LocalTime cutoffTime = LocalTime.of(20, 0); // 8 PM cutoff time
        LocalTime now = LocalTime.now();

        if ((startDate.equals(today) && now.isAfter(cutoffTime))) {
            throw new IllegalArgumentException("Cannot book meals for past dates or after 8 PM for today.");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 90) {
            throw new IllegalArgumentException("Cannot book meals for more than 90 days.");
        }

        List<MealBooking> bookedMeals = new ArrayList<>();
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!isWeekend(date) && !isHoliday(date) && !isAlreadyBooked(userId, date)) {
                MealBooking mealBooking = new MealBooking();
                mealBooking.setUserId(userId);
                mealBooking.setStartDate(startDate);
                mealBooking.setEndDate(endDate);
                mealBooking.setToken(UUID.randomUUID()); // Generate unique token for each booking
                mealBooking.setDate(date); // Set the date for this specific booking
                bookedMeals.add(mealBookingRepository.save(mealBooking));
            }
            date = date.plusDays(1);
        }

        return bookedMeals;
    }

    public void cancelMeal(Long mealId) {
        MealBooking mealBooking = mealBookingRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        // Delete the meal booking from the repository
        mealBookingRepository.delete(mealBooking);
    }

    public List<MealBooking> getBookingsByUser(Long userId) {
        return mealBookingRepository.findByUserId(userId);
    }

    public List<MealBooking> getAllBookings() {
        return mealBookingRepository.findAll();
    }

    public List<MealBooking> getBookingsByUserAndIsRedeemed(Long userId, boolean isRedeemed) {
        return mealBookingRepository.findByUserIdAndIsRedeemed(userId, isRedeemed);
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    private boolean isAlreadyBooked(Long userId, LocalDate date) {
        return mealBookingRepository.existsByUserIdAndDate(userId, date);
    }

    public void redeemMeal(Long mealId) {
        MealBooking mealBooking = mealBookingRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
        mealBooking.setIsRedeemed(true);
        mealBookingRepository.save(mealBooking);
    }

    public MealBooking getMealById(Long mealId) {
        return mealBookingRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
    }
}
