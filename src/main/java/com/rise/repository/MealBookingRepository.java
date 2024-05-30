package com.rise.repository;

import com.rise.entity.MealBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealBookingRepository extends JpaRepository<MealBooking, Long> {
    List<MealBooking> findByUserId(Long userId);
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
    List<MealBooking> findByUserIdAndIsRedeemed(Long userId, boolean isRedeemed);


}
