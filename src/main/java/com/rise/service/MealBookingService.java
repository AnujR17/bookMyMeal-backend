package com.rise.service;


import com.rise.entity.MealBooking;
import com.rise.repository.MealBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealBookingService {

    private final MealBookingRepository mealBookingRepository;

    @Autowired
    public MealBookingService(MealBookingRepository mealBookingRepository) {
        this.mealBookingRepository = mealBookingRepository;
    }

    public void bookMeal(MealBooking mealBooking) {
        mealBookingRepository.save(mealBooking);
    }
}