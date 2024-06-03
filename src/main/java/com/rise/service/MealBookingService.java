package com.rise.service;

import com.rise.entity.MealBooking;
import com.rise.repository.MealBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Defined holidays
    private static final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 25),
            LocalDate.of(2024, 6, 17),
            LocalDate.of(2024,8,15)

    );

    public List<MealBooking> bookMultipleDays(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Start date must be at least tomorrow.");
        }

        LocalTime cutoffTime = LocalTime.of(20, 0); // 8 PM deadline for quick booking
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
                mealBooking.setToken(UUID.randomUUID()); // Generating unique token for each booking
                mealBooking.setDate(date);
                bookedMeals.add(mealBookingRepository.save(mealBooking));
            }
            date = date.plusDays(1);
        }

        return bookedMeals;
    }

    public void cancelMeal(Long mealId) {
        LocalDate bookingDate = mealBookingRepository.findById(mealId)
                .map(MealBooking::getDate)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        // 10 pm deadline for cancellation
        LocalDateTime deadline = LocalDateTime.of(bookingDate.minusDays(1), LocalTime.of(22, 00));
        if (LocalDateTime.now().isBefore(deadline)) {
            MealBooking mealBooking = mealBookingRepository.findById(mealId)
                    .orElseThrow(() -> new RuntimeException("Meal not found"));

            mealBookingRepository.delete(mealBooking);
        } else {
            throw new IllegalStateException("Cancellation window has ended.");
        }
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
