package com.rise.repository;

import com.rise.entity.MealBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealBookingRepository extends JpaRepository<MealBooking, Long> {

}
