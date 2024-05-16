package com.rise.repository;

import com.rise.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal,Long> {
    List<Meal> findByDate(LocalDate date);
}
