package com.rise.repository;


import com.rise.entity.MealBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MealBookingRepository extends JpaRepository<MealBooking, Long> {
    List<MealBooking> findByUserId(Long userId);
}
