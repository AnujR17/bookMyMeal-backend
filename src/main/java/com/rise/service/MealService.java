package com.rise.service;

import com.rise.entity.Meal;
import com.rise.entity.MealBooking;
import com.rise.entity.Token;
import com.rise.repository.MealBookingRepository;
import com.rise.repository.MealRepository;
import com.rise.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MealService {
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Meal bookMeal(Meal meal) {
        UUID uniqueToken = UUID.randomUUID();
        meal.setToken(uniqueToken);
        mealRepository.save(meal);

        Token token = new Token();
        token.setMealId(meal.getMealId());
        token.setToken(uniqueToken);
        tokenRepository.save(token);

        return meal;
    }

    public List<Meal> getMealsByDate(LocalDate date) {
        return mealRepository.findByDate(date);
    }
    public void cancelMeal(Long mealId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new RuntimeException("Meal not found"));
        meal.setCanceled(true);
        mealRepository.save(meal);
    }
}
