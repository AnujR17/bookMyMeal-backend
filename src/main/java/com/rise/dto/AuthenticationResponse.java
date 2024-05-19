package com.rise.dto;

import com.MEALPROJECT.MEAL_PROJECT1.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private UserRole userRole;

    private Long userId;

}
