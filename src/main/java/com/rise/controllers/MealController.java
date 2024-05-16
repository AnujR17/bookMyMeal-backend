    package com.rise.controllers;


    import com.rise.entity.Meal;
    import com.rise.service.MealService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.util.List;


    @RestController
    @RequestMapping("/api/meals")
    public class MealController {
        @Autowired
        private MealService mealService;

        @PostMapping
        public ResponseEntity<Meal> bookMeal(@RequestBody Meal meal) {
            Meal bookedMeal = mealService.bookMeal(meal);
            return ResponseEntity.ok(bookedMeal);
        }

        @GetMapping("/{date}")
        public ResponseEntity<List<Meal>> getMealsByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
            List<Meal> meals = mealService.getMealsByDate(date);
            return ResponseEntity.ok(meals);
        }

        @DeleteMapping("/{mealId}")
        public ResponseEntity<String> cancelMeal(@PathVariable Long mealId) {
            mealService.cancelMeal(mealId);
            return ResponseEntity.ok("Meal canceled successfully");
        }
    }
