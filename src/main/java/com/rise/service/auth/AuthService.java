package com.rise.service.auth;

import com.MEALPROJECT.MEAL_PROJECT1.dtos.SignupRequest;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);



}
