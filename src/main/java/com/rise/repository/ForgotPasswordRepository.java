package com.rise.repository;


import com.rise.entity.ForgotPassword;
import com.rise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp =?1 and fp.user=?2") //jpql query
    Optional<ForgotPassword> findByOtpAndUser(int otp, User user);


    ForgotPassword findByUser(User user);
}
