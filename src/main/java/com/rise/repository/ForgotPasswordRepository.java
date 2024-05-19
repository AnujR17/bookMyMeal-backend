package com.rise.repository;

import com.MEALPROJECT.MEAL_PROJECT1.entities.ForgotPassword;
import com.MEALPROJECT.MEAL_PROJECT1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp =?1 and fp.user=?2") //jpql query
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);


}
